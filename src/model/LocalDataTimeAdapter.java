package model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDataTimeAdapter extends XmlAdapter<String, LocalDateTime>{

	@Override
	public LocalDateTime unmarshal(String str) throws Exception {
		// TODO Auto-generated method stub
		return LocalDateTime.parse(str);
	}

	@Override
	public String marshal(LocalDateTime ldt) throws Exception {
		// TODO Auto-generated method stub
		return ldt.toString();
	}

}
