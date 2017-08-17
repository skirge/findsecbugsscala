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
package org.sonar.plugins.findbugs.profiles;

import org.sonar.plugins.scala.Scala;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.findbugs.FindbugsProfileImporter;
import org.sonar.plugins.findbugs.scala.FindbugsScalaProfileImporter;
import org.sonar.plugins.java.Java;

import java.io.InputStreamReader;
import java.io.Reader;

public class FindbugsScalaProfile extends ProfileDefinition {

    private static final String FINDBUGS_PROFILE_NAME = "FindBugs (Scala)";
    private final FindbugsScalaProfileImporter importer;

    public FindbugsScalaProfile(FindbugsScalaProfileImporter importer) {
        this.importer = importer;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages messages) {
        Reader findbugsProfile = new InputStreamReader(this.getClass().getResourceAsStream(
                "/org/sonar/plugins/findbugs/profile-findbugs-only.xml"));
        RulesProfile profile = importer.importProfile(findbugsProfile, messages);
        profile.setLanguage(Scala.KEY);
        profile.setName(FINDBUGS_PROFILE_NAME);
        return profile;
    }

}
