/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.command.CommandGoal;
import org.eclipse.che.ide.api.command.CommandType;
import org.eclipse.che.ide.api.command.CommandTypeRegistry;
import org.eclipse.che.ide.api.command.ContextualCommand;
import org.eclipse.che.ide.api.command.PredefinedCommandGoalRegistry;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.ui.SVGResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A smattering of useful methods to work with commands.
 *
 * @author Artem Zatsarynnyi
 */
@Singleton
public class CommandUtils {

    private final PredefinedCommandGoalRegistry goalRegistry;
    private final CommandTypeRegistry           commandTypeRegistry;
    private final IconRegistry                  iconRegistry;

    @Inject
    public CommandUtils(PredefinedCommandGoalRegistry predefinedCommandGoalRegistry,
                        CommandTypeRegistry commandTypeRegistry,
                        IconRegistry iconRegistry) {
        this.goalRegistry = predefinedCommandGoalRegistry;
        this.commandTypeRegistry = commandTypeRegistry;
        this.iconRegistry = iconRegistry;
    }

    /**
     * Groups the given {@code commands} by its goal.
     *
     * @return map that contains the given {@code commands} grouped by its goal
     */
    public Map<CommandGoal, List<ContextualCommand>> groupCommandsByGoal(List<ContextualCommand> commands) {
        final Map<CommandGoal, List<ContextualCommand>> commandsByGoal = new HashMap<>();

        for (ContextualCommand command : commands) {
            final String goalId = command.getGoal();
            final CommandGoal commandGoal = goalRegistry.getGoalForId(goalId);

            commandsByGoal.computeIfAbsent(commandGoal, key -> new ArrayList<>())
                          .add(command);
        }

        return commandsByGoal;
    }

    /** Returns the icon for the given command type ID or {@code null} if none. */
    @Nullable
    public SVGResource getCommandTypeIcon(String typeId) {
        final CommandType commandType = commandTypeRegistry.getCommandTypeById(typeId);

        if (commandType != null) {
            final Icon icon = iconRegistry.getIconIfExist("command.type." + commandType.getId());

            if (icon != null) {
                final SVGImage svgImage = icon.getSVGImage();

                if (svgImage != null) {
                    return icon.getSVGResource();
                }
            }
        }

        return null;
    }

    /** Returns the icon for the given command goal ID or {@code null} if none. */
    @Nullable
    public SVGResource getCommandGoalIcon(String goalId) {
        final Optional<CommandGoal> goalOptional = goalRegistry.getGoalById(goalId);

        if (goalOptional.isPresent()) {
            final Icon icon = iconRegistry.getIconIfExist("command.goal." + goalOptional.get().getId());

            if (icon != null) {
                final SVGImage svgImage = icon.getSVGImage();

                if (svgImage != null) {
                    return icon.getSVGResource();
                }
            }
        }

        return null;
    }
}
