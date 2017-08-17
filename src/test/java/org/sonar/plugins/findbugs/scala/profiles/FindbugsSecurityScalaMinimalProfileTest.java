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

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.findbugs.FindbugsProfileImporter;
import org.sonar.plugins.findbugs.scala.FindbugsScalaProfileImporter;
import org.sonar.plugins.findbugs.profiles.FindbugsSecurityMinimalProfile;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsScalaRulesDefinition;

import static org.fest.assertions.Assertions.assertThat;

public class FindbugsSecurityScalaMinimalProfileTest {

    @Test
    public void shouldCreateProfile() {
        FindbugsScalaProfileImporter importer = new FindbugsScalaProfileImporter(org.sonar.plugins.findbugs.rules.FakeRuleScalaFinder.createWithAllRules());
        org.sonar.plugins.findbugs.profiles.FindbugsSecurityScalaMinimalProfile secOnlyProfile = new org.sonar.plugins.findbugs.profiles.FindbugsSecurityScalaMinimalProfile(importer);
        ValidationMessages validation = ValidationMessages.create();
        RulesProfile profile = secOnlyProfile.createProfile(validation);


        assertThat(validation.getErrors()).isEmpty();
        assertThat(validation.getWarnings()).isEmpty();
        // The standard FindBugs include only 9. Fb-Contrib and FindSecurityBugs include other rules
        assertThat(profile.getActiveRulesByRepository(FindbugsScalaRulesDefinition.REPOSITORY_KEY)).hasSize(8);
        // 62 rules total - 20 informational = 42 major or critical
        assertThat(profile.getActiveRulesByRepository(FindSecurityBugsScalaRulesDefinition.REPOSITORY_KEY)).hasSize(87);
    }
}
