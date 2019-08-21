/*
 * -----------------------------------------------------------------------------
 *  Copyright (C) 2019 University of Dundee & Open Microscopy Environment.
 *  All rights reserved.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * ------------------------------------------------------------------------------
 */
package org.openmicroscopy.api

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.openmicroscopy.api.tasks.SplitTask
import org.openmicroscopy.api.types.Language

@CompileStatic
class ApiPlugin implements Plugin<Project> {

    private Project project

    @Override
    void apply(Project project) {
        this.project = project

        // Apply our base plugin
        project.plugins.apply(ApiPluginBase)

        configureForJavaPlugin()
    }

    void configureForJavaPlugin() {
        project.plugins.withType(JavaPlugin) { JavaPlugin java ->
            project.tasks.withType(SplitTask).configureEach { SplitTask splitTask ->
                if (splitTask.language.get() == Language.JAVA) {
                    project.tasks.getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME) { Task compileJava ->
                        compileJava.dependsOn(splitTask)
                    }
                }
            }
        }
    }

}
