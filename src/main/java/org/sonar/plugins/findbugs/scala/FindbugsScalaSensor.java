package org.sonar.plugins.findbugs.scala;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.plugins.findbugs.FindbugsExecutor;
import org.sonar.plugins.findbugs.FindbugsSensor;
import org.sonar.plugins.findbugs.resource.ByteCodeResourceLocator;
import org.sonar.plugins.findbugs.rules.FbContribScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsScalaRulesDefinition;
import org.sonar.plugins.java.api.JavaResourceLocator;

public class FindbugsScalaSensor extends FindbugsSensor {

    public FindbugsScalaSensor(RulesProfile profile, ActiveRules ruleFinder, SensorContext sensorContext, FindbugsExecutor executor, JavaResourceLocator javaResourceLocator, FileSystem fs, ByteCodeResourceLocator byteCodeResourceLocator) {
        super(profile, ruleFinder, sensorContext, executor, javaResourceLocator, fs, byteCodeResourceLocator);
        super.registerRepositories(FindbugsScalaRulesDefinition.REPOSITORY_KEY,
                FbContribScalaRulesDefinition.REPOSITORY_KEY,
                FindSecurityBugsScalaRulesDefinition.REPOSITORY_KEY);
    }

}
