package it.mfm.model;

public class Widget {
	
    /**
     * Il nome del widget
     */
	private String name;
	
	/**
	 * Il repository del widget
	 */
	private String repository;
	
	/**
	 * La versione del repository che si vuole utilizzare
	 */
	private String version;
	
	/**
	 * Il nome della directory locale che viene generato
	 */
	private String localDir;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getLocalDir() {
        return localDir;
    }
    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }
}
