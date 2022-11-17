package com.sliit.ssd.client.model;

public class File {
		private int file_id;
	  	private String fileName;
	    private String downloadUri;
	    private long size;
	    
	    
		public File() {
			super();
		}
		public int getFile_id() {
			return file_id;
		}
		public void setFile_id(int file_id) {
			this.file_id = file_id;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getDownloadUri() {
			return downloadUri;
		}
		public void setDownloadUri(String downloadUri) {
			this.downloadUri = downloadUri;
		}
		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
	    
}
