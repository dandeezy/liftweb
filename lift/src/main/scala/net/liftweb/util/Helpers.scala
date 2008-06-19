package net.liftweb.util

/* 
 * Copyright 2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * The Helpers object provides a lot of utility functions:<ul>
 * <li>Time and date functions: 
 * <li>URL functions 
 * </ul>
 */
object Helpers extends TimeHelpers with StringHelpers with ListHelpers 
                                   with SecurityHelpers with BindHelpers with HttpHelpers 
                                   with IoHelpers with BasicTypesHelpers 
                                   with ClassHelpers with ControlHelpers
