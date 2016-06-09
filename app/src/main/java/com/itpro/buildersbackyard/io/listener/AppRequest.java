/*
 *
 *  * Copyright (c) 2015, 360ITPRO and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions
 *  * are met:
 *  *
 *  *   - Redistributions of source code must retain the above copyright
 *  *     notice, this list of conditions and the following disclaimer.
 *  *
 *  *   - Redistributions in binary form must reproduce the above copyright
 *  *     notice, this list of conditions and the following disclaimer in the
 *  *     documentation and/or other materials provided with the distribution.
 *  *
 *  */
package com.itpro.buildersbackyard.io.listener;

import com.itpro.buildersbackyard.io.http.BaseTask;

/**
 * @author RIPAN SHARMA
 */
public interface AppRequest {

	public <T> void onRequestStarted(BaseTask<T> listener);
	
	public <T> void onRequestCompleted(BaseTask<T> listener);
	
	public <T> void onRequestFailed(BaseTask<T> listener);
	
}
