/*
 * SonarQube Findbugs Plugin
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.findbugs.scala;

import org.sonar.api.Plugin;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.plugins.findbugs.FindbugsConfiguration;
import org.sonar.plugins.findbugs.language.Jsp;
import org.sonar.plugins.findbugs.language.JspSyntaxSensor;
import org.sonar.plugins.findbugs.profiles.*;
import org.sonar.plugins.findbugs.profiles.FindbugsScalaContribProfile;
import org.sonar.plugins.findbugs.profiles.FindbugsScalaProfile;
import org.sonar.plugins.findbugs.profiles.FindbugsSecurityAuditScalaProfile;
import org.sonar.plugins.findbugs.profiles.FindbugsSecurityScalaMinimalProfile;
import org.sonar.plugins.findbugs.resource.ByteCodeResourceLocator;
import org.sonar.plugins.findbugs.rules.*;
import org.sonar.plugins.findbugs.rules.FbContribScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsScalaRulesDefinition;
import org.sonar.plugins.java.Java;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindbugsScalaPlugin implements Plugin {


    public void define(Context context) {
        //context.addExtensions(FindbugsConfiguration.getPropertyDefinitions());

        context.addExtensions(Arrays.asList(
                FindbugsScalaContribProfile.class,
                FindbugsScalaProfile.class,
                FindbugsSecurityAuditScalaProfile.class,
                FindbugsSecurityScalaMinimalProfile.class,
                FbContribScalaRulesDefinition.class,
                FindbugsScalaRulesDefinition.class,
                FindSecurityBugsScalaRulesDefinition.class,
                FindbugsScalaPlugin.class,
                FindbugsScalaProfileImporter.class,
                FindbugsScalaSensor.class
        ));
    }
}
