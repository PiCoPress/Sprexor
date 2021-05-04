package sprexor.v2;

import java.util.LinkedHashMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

class SprexorLoader {
	private static final LinkedHashMap<String, String> reg = new LinkedHashMap<String, String>();
	private static final XPath xp = XPathFactory.newInstance().newXPath();
	Document doc;
	SprexorLoader(Document doc) throws XPathExpressionException{
		final String[] lstr = xp.evaluate("//load", doc).split("\n");
		for(String s : lstr) {
			if(s.isBlank()) continue;
			s = s.trim();
			reg.put(s, xp.evaluate("//" + s, doc).trim());
		}
	}
	String get(final String key) {
		return reg.get(key);
	}
}
