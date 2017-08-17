package org.sonar.plugins.findbugs.profiles;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.findbugs.FindbugsProfileImporter;
import org.sonar.plugins.findbugs.scala.FindbugsScalaProfileImporter;
import org.sonar.plugins.scala.Scala;
import org.sonar.plugins.java.Java;

import java.io.InputStreamReader;
import java.io.Reader;

public class FindbugsScalaContribProfile extends ProfileDefinition {

    private static final String FB_CONTRIB_PROFILE_NAME = "FindBugs + FB-Contrib (Scala)";
    private final FindbugsScalaProfileImporter importer;

    public FindbugsScalaContribProfile(FindbugsScalaProfileImporter importer) {
        this.importer = importer;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages messages) {
        Reader findbugsProfile = new InputStreamReader(this.getClass().getResourceAsStream(
                "/org/sonar/plugins/findbugs/profile-findbugs-and-fb-contrib.xml"));
        RulesProfile profile = importer.importProfile(findbugsProfile, messages);
        profile.setLanguage(Scala.KEY);
        profile.setName(FB_CONTRIB_PROFILE_NAME);
        return profile;
    }

}
