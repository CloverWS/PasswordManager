package model;



import javax.xml.bind.annotation.adapters.XmlAdapter;

import javafx.scene.paint.Color;

public class ColorAdapter extends XmlAdapter<String, Color>{

	@Override
	public Color unmarshal(String colorName) throws Exception {
		// TODO Auto-generated method stub
		return Color.valueOf(colorName);
	}

	@Override
	public String marshal(Color color) throws Exception {
		// TODO Auto-generated method stub
		return color.toString();
	}

}
