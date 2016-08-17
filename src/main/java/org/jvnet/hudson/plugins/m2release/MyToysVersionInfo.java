package org.jvnet.hudson.plugins.m2release;

import java.util.List;
import java.util.Vector;

import org.apache.maven.shared.release.versions.DefaultVersionInfo;
import org.apache.maven.shared.release.versions.VersionInfo;
import org.apache.maven.shared.release.versions.VersionParseException;

public class MyToysVersionInfo extends DefaultVersionInfo implements
		VersionInfo {

	public MyToysVersionInfo(List<String> digits, String annotation,
			String annotationRevision, String buildSpecifier,
			String annotationSeparator, String annotationRevSeparator,
			String buildSeparator) {
		super(digits, annotation, annotationRevision, buildSpecifier,
				annotationSeparator, annotationRevSeparator, buildSeparator);
	}
	
	public  MyToysVersionInfo(String version) throws VersionParseException {
		super(version);
	}

	@Override
	public VersionInfo getNextVersion() {
		MyToysVersionInfo version = null;
		List<String> digits = new Vector<String>(getDigits());
		

		// digits.set( digits.size() - 1, incrementVersionString( digits.get(
		// digits.size() - 1 ) ) );
		digits.set(1, incrementVersionString(digits.get(1)));
		for (int i=2;i<digits.size();i++) {
			digits.set(i, "0");
		}

		version = new MyToysVersionInfo(digits, getAnnotation(),
				getAnnotationRevision(), getBuildSpecifier(), "-", "-", "-");
		return version;
	}

	@Override
	public String getReleaseVersionString() {
		String baseVersion = getVersionString(this, getBuildSpecifier(), "-");
		int betaIndex = baseVersion.indexOf("-BETA");
		int alphaIndex = baseVersion.indexOf("-ALPHA");
		if ( betaIndex >= 0 ) {
			return baseVersion.substring(0,betaIndex);
		}
		//if ( alphaIndex >= 0 ) {
		//	return baseVersion.substring(0, alphaIndex);
		//}
		return super.getReleaseVersionString();
	}

	@Override
	public String getSnapshotVersionString() {
		String baseVersion = getVersionString(this, getBuildSpecifier(), "-");
		int alphaIndex = baseVersion.indexOf("-ALPHA");
		int betaIndex = baseVersion.indexOf("-BETA");
		String releaseVersion = getReleaseVersionString();
		if ( alphaIndex >= 0 ) {
			return releaseVersion + "-SNAPSHOT";
		}
		if ( betaIndex >= 0 ) {
			return releaseVersion + "-BETA-SNAPSHOT";
		}
		return baseVersion + "-BETA-SNAPSHOT";
	}
	
	public static void main(String[] args) throws VersionParseException {
		for (String s: new String[] {"1.5", "1.5.7", "1.5.7-BETA-SNAPSHOT", "1.5.7-ALPHA-FOOBAR-SNAPSHOT"}) {
			MyToysVersionInfo v = new MyToysVersionInfo(s);
			System.out.println(v + " ###");
			System.out.println(v.getReleaseVersionString());
			System.out.println(v.getSnapshotVersionString());
			System.out.println(v.getNextVersion());
		}
	}
}
