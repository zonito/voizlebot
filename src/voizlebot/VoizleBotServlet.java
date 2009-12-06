package voizlebot;

import java.net.*;
import java.io.*;

import com.google.wave.api.AbstractRobotServlet;
import com.google.wave.api.Blip;
import com.google.wave.api.Event;
import com.google.wave.api.EventType;
import com.google.wave.api.Range;
import com.google.wave.api.RobotMessageBundle;
import com.google.wave.api.TextView;
import com.google.wave.api.Wavelet;

public class VoizleBotServlet extends AbstractRobotServlet {

	private static final long serialVersionUID = 1L;
	private boolean flag = false;
	@Override
	public void processEvents(RobotMessageBundle bundle) {
		Wavelet wavelet = bundle.getWavelet();

		if (bundle.wasSelfAdded()) {
			Blip blip = wavelet.appendBlip();
			TextView textView = blip.getDocument();
			textView
					.appendMarkup("Hello, I am VoizleBot, Helps you in shortening long url to short url. "
							+ "I will short Long url within your blip automatically.");
		}
		for (Event e : bundle.getEvents()) {
			if (e.getType() == EventType.BLIP_SUBMITTED) {
				extractUrl(e);
			}

		}
	}

	private void extractUrl(Event e) {
		TextView content = e.getBlip().getDocument();

		String blipText = content.getText();
		StringBuilder sb = makeContent(blipText);
		if (flag) {
			Range r = new Range();
			r.setStart(0);
			r.setEnd(blipText.length());
			content.replace(r, sb.toString());
		}
	}

	private StringBuilder makeContent(String msg) {
		String[] msgs = msg.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String m : msgs) {
			final int first_dot = m.indexOf(".");
			final int last_dot = m.lastIndexOf(".");
			if ((m.startsWith("http://") || m.startsWith("https://"))
					&& m.length() > 12 && first_dot > -1
					&& first_dot != last_dot && (last_dot + 1) != m.length()
					&& !m.startsWith("http://u.voizle.com")) {
				flag = true;
				sb.append("http://u.voizle.com/" + getId(m));
			} else {
				sb.append(m);
			}
			sb.append(" ");
		}
		return sb;
	}

	private String getId(String url) {
		try {
			final URL newurl = new URL(
					"http://api.voizle.com/?type=short&crawl=no&u=" + url
							+ "&format=json&property=urlid");
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					newurl.openStream()));

			String inputLine, text = "";

			while ((inputLine = in.readLine()) != null)
				text += inputLine;

			in.close();
			return text;
		} catch (Exception ex) {
			return "voizle";
		}
	}

}
