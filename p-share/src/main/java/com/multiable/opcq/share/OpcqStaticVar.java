package com.multiable.opcq.share;

public class OpcqStaticVar {
	public static final String appName = "opcq";
	public static final String Opcq = "opcq";

	public static class OpcqEJB {
		public static final String BookEJB = appName + "/BookEJB";
	}

	public static class OpcqRESTFUL {
		public static final String book = Opcq + "/book";
	}
}
