/*
{
	"id": "polimi",
    "latitude": 45.4786583,
    "longitude": 9.2261817,
    "zoom": 15,
    "name": "Polimi",
    "service": "polimi",
    "refEnabler": "city",
    "imageUrl": "polimi.jpg"
}
*/

package it.eng.tools.model;

public class ScopeEntityBean {
	
	private String id;
	private String latitude;
	private String longitude;
	private String zoom;
	private String name; 
	private String service; 
	private String refEnabler;
	private String imageUrl;
	
	public ScopeEntityBean() {
		super();
	}

	public ScopeEntityBean(String id, String latitude, String longitude, String zoom, String name, String service,
			String refEnabler, String imageUrl) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zoom = zoom;
		this.name = name;
		this.service = service;
		this.refEnabler = refEnabler;
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getZoom() {
		return zoom;
	}

	public void setZoom(String zoom) {
		this.zoom = zoom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getRefEnabler() {
		return refEnabler;
	}

	public void setRefEnabler(String refEnabler) {
		this.refEnabler = refEnabler;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ScopeEntityBean [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", zoom=" + zoom
				+ ", name=" + name + ", service=" + service + ", refEnabler=" + refEnabler + ", imageUrl=" + imageUrl
				+ "]";
	}
	
	
	
	
	
	
	
	

}
