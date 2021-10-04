package it.eng.iot.servlet.model;

import it.eng.iot.configuration.ConfOrionCB;

public class MapCenter {
		
	private Double lat;
	private Double lng;
	private Integer zoom;
	
	public MapCenter() {
		this.lat = 0.0;
		this.lng = 0.0;
		this.zoom = Integer.parseInt(ConfOrionCB.getInstance().getString("orion.mapcenter.zoom"));
	}
	
	public MapCenter(Double lat, Double lng, Integer zoom) {
		this.lat = lat;
		this.lng = lng;
		this.zoom = zoom;
	}
	
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
}
