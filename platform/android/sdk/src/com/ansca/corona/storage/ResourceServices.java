//////////////////////////////////////////////////////////////////////////////
//
// Copyright (C) 2018 Corona Labs Inc.
// Contact: support@coronalabs.com
//
// This file is part of the Corona game engine.
//
// Commercial License Usage
// Licensees holding valid commercial Corona licenses may use this file in
// accordance with the commercial license agreement between you and 
// Corona Labs Inc. For licensing terms and conditions please contact
// support@coronalabs.com or visit https://coronalabs.com/com-license
//
// GNU General Public License Usage
// Alternatively, this file may be used under the terms of the GNU General
// Public license version 3. The license is as published by the Free Software
// Foundation and appearing in the file LICENSE.GPL3 included in the packaging
// of this file. Please review the following information to ensure the GNU 
// General Public License requirements will
// be met: https://www.gnu.org/licenses/gpl-3.0.html
//
// For overview and more information on licensing please refer to README.md
//
//////////////////////////////////////////////////////////////////////////////

package com.ansca.corona.storage;


/**
 * Provides easy access to the Android generated "R" resource class and its contained resources.
 * <p>
 * This is needed because the JAR file version of Corona does not have a "R" resource class and
 * needs to access it via the package name. Corona templates cannot access this resource class
 * via the package name since it can change via the AndroidManifest.xml, so it must access this class
 * by its namespace instead.
 */
public class ResourceServices extends com.ansca.corona.ApplicationContextProvider {
	/**
	 * Value used to indicate an invalid resource ID retrieved via Corona's ResourceServices.getResourceId() method
	 * or Android's android.content.res.Resources.getIdentifier() method.
	 */
	public static final int INVALID_RESOURCE_ID = 0;
	
	/** The package name (ie: namespace) that the "R" resource class resides in. */
	private static String sPackageName = null;
	
	/** Stores a reference to the Android generated "R" resource class. */
	private static Class sRClass = null;
	
	
	/**
	 * Creates an object that provides easy access to the application's resources.
	 * @param context Reference to an Android created context used to access the input device system.
	 *                <p>
	 *                Setting this to null will cause an exception to be thrown.
	 */
	public ResourceServices(android.content.Context context) {
		super(context);
	}

	/**
	 * Fetches the package name to load resources from via Android's Resources class.
	 * @return Returns the package name to load resources from.
	 *         For example, this function will return "com.ansca.corona" if this is the Corona template build.
	 */
	public String getPackageName() {
		// Fetch the package name that contains Corona's resources, if not done already.
		if (sPackageName == null) {
			sPackageName = "";
			Class rClass = getRClass();
			if (rClass != null) {
				Package packageInfo = rClass.getPackage();
				if (packageInfo != null) {
					sPackageName = packageInfo.getName();
				}
			}
		}
		
		// Return the name of the package.
		return sPackageName;
	}
	
	/**
	 * Fetches the application's R class that was generated by the Android SDK when building the APK.
	 * @return Returns the R class to be used to fetch this application's resources.
	 */
	public Class getRClass() {
		// Fetch the R class if not done already.
		if (sRClass == null) {
			// First attempt to fetch the application's R class via its package name.
			// This will only succeed if Corona is a JAR library linked to a 3rd party application.
			try { sRClass = Class.forName(getApplicationContext().getPackageName() + ".R"); }
			catch (Exception ex) { }
			
			// If an R class was not found, then this is a Corona template.
			// Fetch its generated R class based on its original package name (ie: namespace) used to compile the template.
			// Note: Fetching Corona's generated R class must be done last because there is a bug in Android's APIs where
			//       they fail to fetch resources from this R class if Corona was loaded as a JAR library.
			if (sRClass == null) {
				try { sRClass = Class.forName(com.ansca.corona.CoronaEnvironment.class.getPackage().getName() + ".R"); }
				catch (Exception ex) { }
			}
		}
		
		// Return the R class.
		return sRClass;
	}
	
	/**
	 * Fetches this application's Resources object used to access resource files.
	 * @return Returns this application's Resources object.
	 *         Returns null if unable to access this application's resources.
	 */
	public android.content.res.Resources getResources() {
		return getApplicationContext().getResources();
	}
	
	/**
	 * Fetches the unique integer ID of the specified resource.
	 * @param resourceName The unique name of the resource. Cannot be null or empty.
	 *                     For resource files, this should be the name of the file, minus the extension.
	 *                     For string resources, this is the key to the string in the table.
	 * @param typeName The unique name of the type of resource such as "drawable", "layout", "string", etc.
	 * @return Returns the unique integer ID assigned to the specified resource.
	 *         This is the ID assigned to the R class that is generated by the Android SDK when building an application.
	 *         Returns ResourceServices.INVALID_RESOURCE_ID if the resource was not found.
	 */
	public int getResourceId(String resourceName, String typeName) {
		// Validate.
		if ((resourceName == null) || (resourceName.length() <= 0)) {
			return INVALID_RESOURCE_ID;
		}
		
		// Fetch the specified resource's unique integer ID by its name.
		android.content.res.Resources resources = getResources();
		if (resources == null) {
			return INVALID_RESOURCE_ID;
		}
		return resources.getIdentifier(resourceName, typeName, getPackageName());
	}
	
	/**
	 * Fetches the unique integer ID assigned to an image in the application's resource file.
	 * @param resourceName The unique name of the image file, minus the extension. Cannot be null or empty.
	 * @return Returns the unique integer ID assigned to the specified resource.
	 *         This is the ID assigned to the R class that is generated by the Android SDK when building an application.
	 *         Returns ResourceServices.INVALID_RESOURCE_ID if the resource was not found.
	 */
	public int getDrawableResourceId(String resourceName) {
		return getResourceId(resourceName, "drawable");
	}
	
	/**
	 * Fetches the unique integer ID assigned to a layout in this application's resource file.
	 * @param resourceName The unique name of the layout file, minus the extension. Cannot be null or empty.
	 * @return Returns the unique integer ID assigned to the specified resource.
	 *         This is the ID assigned to the R class that is generated by the Android SDK when building an application.
	 *         Returns ResourceServices.INVALID_RESOURCE_ID if the resource was not found.
	 */
	public int getLayoutResourceId(String resourceName) {
		return getResourceId(resourceName, "layout");
	}

	/**
	 * Fetches the unique integer ID assigned to a file in this application's "raw" resource directory.
	 * @param resourceName The unique name of the file, minus the extension. Cannot be null or empty.
	 * @return Returns the unique integer ID assigned to the specified resource.
	 *         This is the ID assigned to the R class that is generated by the Android SDK when building an application.
	 *         Returns ResourceServices.INVALID_RESOURCE_ID if the resource was not found.
	 */
	public int getRawResourceId(String resourceName) {
		return getResourceId(resourceName, "raw");
	}
	
	/**
	 * Fetches the unique integer ID assigned to a string in this application's string table.
	 * @param resourceName The unique name of the string resource in the string table. Cannot be null or empty.
	 * @return Returns the unique integer ID assigned to the specified resource.
	 *         This is the ID assigned to the R class that is generated by the Android SDK when building an application.
	 *         Returns ResourceServices.INVALID_RESOURCE_ID if the resource was not found.
	 */
	public int getStringResourceId(String resourceName) {
		return getResourceId(resourceName, "string");
	}
}
