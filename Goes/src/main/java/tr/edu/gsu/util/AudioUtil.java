package tr.edu.gsu.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.terminal.StreamResource;

public class AudioUtil {
	private static Log LOG = LogFactory.getLog(AudioUtil.class);

	public static byte[] convertStreamResource2Bytes(StreamResource audio) {
		try {
			if (audio == null)
				return null;
			InputStream iStream = audio.getStreamSource().getStream();
			return IOUtils.toByteArray(iStream);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static StreamResource convertBytes2StreamResource(final byte[] audioBytes) {
		if (audioBytes == null)
			return null;
		return new StreamResource(new StreamResource.StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				return new ByteArrayInputStream(audioBytes);
			}
		}, generateFileName(), AppContext.getApp());
	}

	private static SecureRandom random = new SecureRandom();

	private static String generateFileName() {
		long n = random.nextLong();
		return "audio" + Long.toString(n) + ".mp3";
	}
}
