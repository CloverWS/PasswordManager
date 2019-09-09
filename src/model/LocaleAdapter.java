package model;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocaleAdapter extends XmlAdapter<String, Locale>{

	@Override
	public Locale unmarshal(String languageString) throws Exception {
		// TODO Auto-generated method stub
		return new Locale(languageString);
	}

	@Override
	public String marshal(Locale language) throws Exception {
		// TODO Auto-generated method stub
		return language.getLanguage();
	}

}
