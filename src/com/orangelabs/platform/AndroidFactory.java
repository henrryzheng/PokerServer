/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.orangelabs.platform;

import com.orangelabs.platform.network.NetworkFactory;
import com.orangelabs.platform.registry.RegistryFactory;

import android.content.Context;
import android.util.Log;

/**
 * Android platform
 * 
 * @author jexa7410
 */
public class AndroidFactory {
	/**
	 * Android application context
	 */
	private static Context context = null;

	/**
	 * Returns the application context
	 * 
	 * @return Context
	 */
	public static Context getApplicationContext() {
		return context;
	}
	
	/**
	 * Load factory
	 * 
	 * @param context Context
	 */
	public static void setApplicationContext(Context context) {
		
		AndroidFactory.context = context;
		try {

			NetworkFactory.loadFactory("com.orangelabs.platform.network.AndroidNetworkFactory");
			RegistryFactory.loadFactory("com.orangelabs.platform.registry.AndroidRegistryFactory");
			
			// FileFactory.loadFactory("com.orangelabs.platform.file.AndroidFileFactory");
		
		} catch(FactoryException e) {
			e.printStackTrace();
			Log.d("xxx", "setApplicationContext error : "+e);
		}
	}
}
