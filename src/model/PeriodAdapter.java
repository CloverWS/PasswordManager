package model;

import java.time.Period;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PeriodAdapter extends XmlAdapter<String, Period>{

	@Override
	public Period unmarshal(String str) throws Exception {
		// TODO Auto-generated method stub
		return Period.parse(str);
	}

	@Override
	public String marshal(Period period) throws Exception {
		// TODO Auto-generated method stub
		return period.toString();
	}

}
