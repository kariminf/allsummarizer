package as.PreProcess;

import java.util.HashMap;

public interface Normalizer {

	public String normalize (String text);
	public String normalize (String text, String param);
	public HashMap <String, String> getParameters();
}
