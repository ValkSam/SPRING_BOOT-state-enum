package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.ActionConditionFailedException;
import com.example.demo.service.state.exception.ActionParamsNeededException;
import com.example.demo.service.state.exception.ActionParamsNotNeededException;
import com.example.demo.service.state.exception.UnsupportedStatusForActionException;
import com.example.demo.service.state.exception.UnsupportedStatusNameException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by ValkSam
 */
public interface IStatus<T> {

    void initSchema(Map<IStatusAction, IStatus> schemaMap);

    Integer getCode();

    String name();

    Map<IStatusAction, IStatus> getSchemaMap();

    static IStatus getBeginState(Class<? extends IStatus> statusClass) {
        Set<IStatus> allNodesSet = collectAllSchemaMapNodesSet(statusClass);
        List<IStatus> candidateList = Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> !allNodesSet.contains(e))
                .collect(Collectors.toList());
        if (candidateList.size() == 0) {
            throw new AssertionError("begin state not found");
        }
        if (candidateList.size() > 1) {
            throw new AssertionError("more than one begin state found: " + candidateList);
        }
        return candidateList.get(0);
    }

    static Set<IStatus> getEndStatesSet(Class<? extends IStatus> statusClass) {
        return Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> e.getSchemaMap().isEmpty())
                .collect(Collectors.toSet());
    }

    static Set<IStatus> getMiddleStatesSet(Class<? extends IStatus> statusClass) {
        IStatus beginState = getBeginState(statusClass);
        return Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> !e.getSchemaMap().isEmpty())
                .filter(e -> e != beginState)
                .collect(Collectors.toSet());
    }

    static Set<IStatus> collectAllSchemaMapNodesSet(Class<? extends IStatus> statusClass) {
        Set<IStatus> result = new HashSet<>();
        Arrays.stream(statusClass.getEnumConstants())
                .forEach(e -> result.addAll(e.getSchemaMap().values()));
        return result;
    }

    default IStatus nextState(IStatusAction action) {
        if (action.isVerifiable()) {
            throw new ActionParamsNeededException(action.name());
        }
        return nextState(getSchemaMap(), action)
                .orElseThrow(() -> new UnsupportedStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));
    }

    default IStatus nextState(IStatusAction<T> action, T actionParam) {
        IStatus result = nextState(getSchemaMap(), action)
                .orElseThrow(() -> new UnsupportedStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));

        if (!action.isVerifiable()) {
            throw new ActionParamsNotNeededException(action.name());
        }

        List<Predicate<T>> failedPredicates = action.getPredicates().stream()
                .filter(e -> !e.test(actionParam))
                .collect(Collectors.toList());
        if (!failedPredicates.isEmpty()) {
            String message = " failed predicate(s): "
                    + failedPredicates.stream().map(e -> e.getClass().getSimpleName()).collect(Collectors.joining(";"))
                    + " for param: " + actionParam
                    + " when action: " + action.name() + " on state: " + this.name();
            throw new ActionConditionFailedException(message);
        }
        return result;
    }

    default Optional<IStatus> nextState(Map<IStatusAction, IStatus> schemaMap, IStatusAction action) {
        return Optional.ofNullable(schemaMap.get(action));
    }

    default Boolean isAvailableForAction(IStatusAction action) {
        return getSchemaMap().get(action) != null;
    }

    static Set<IStatus> getAvailableForAction(Class<? extends IStatus> statusClass, IStatusAction action) {
        return Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> e.isAvailableForAction(action))
                .collect(Collectors.toSet());
    }

    static Set<IStatus> getAvailableForAction(Class<? extends IStatus> statusClass, Set<IStatusAction> action) {
        return Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> action.stream().anyMatch(action1 -> e.isAvailableForAction(action1)))
                .collect(Collectors.toSet());
    }

    static <T extends IStatus> T convert(Class<T> statusClass, int id) {
        return Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> e.getCode() == id)
                .findAny()
                .orElseThrow(() -> new UnsupportedStatusForActionException(String.valueOf(id)));
    }

    static <T extends IStatus> T convert(Class<T> statusClass, String name) {
        return Arrays.stream(statusClass.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findAny()
                .orElseThrow(() -> new UnsupportedStatusNameException(name));
    }


}
