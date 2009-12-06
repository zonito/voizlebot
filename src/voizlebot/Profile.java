package voizlebot;

import com.google.wave.api.ProfileServlet;

public class Profile extends ProfileServlet {

	@Override
	public String getRobotName() {
		return "VoizleBot";
	}

	@Override
	public String getRobotAvatarUrl() {
		return "http://voizlebot.appspot.com/images/Voizle.png";
	}

	@Override
	public String getRobotProfilePageUrl() {
		return "http://www.voizle.com/";
	}

}
