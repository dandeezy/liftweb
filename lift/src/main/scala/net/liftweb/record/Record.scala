package net.liftweb.record

/* 
* Copyright 2007-2008 WorldWide Conferencing, LLC
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

import net.liftweb._
import util._
import scala.xml._
import net.liftweb.http.js.{JsExp, JE}
import net.liftweb.http.{FieldError, SHtml}
import net.liftweb.mapper.{Safe, KeyObfuscator}
import field._
     
trait Record[MyType <: Record[MyType]] {
  self: MyType =>
  
  /**
   * A unique identifier for this record... used for access control
   */
  private val secure_# = Safe.next

  /**
   * Was this instance deleted from backing store?
   */
  private var was_deleted_? = false
  
  /**
   * The meta record (the object that contains the meta result for this type)
   */
  def meta: MetaRecord[MyType]
  
  /**
   * Is it safe to make changes to the record (or should we check access control?)
   */
  final def safe_? : boolean = {
    Safe.safe_?(secure_#)
  }
  
  /**
   * Save the instance and return the instance
   */
  def save(): MyType = {
    runSafe {
      meta.save(this)
    }
    this
  }
  
  def runSafe[T](f : => T) : T = {
    Safe.runSafe(secure_#)(f)
  }
  
  def asHtml: NodeSeq = {
    meta.asHtml(this)
  }
  
  /**
  * If the instance calculates any additional
  * fields for JSON object, put the calculated fields
  * here
  */
  def suplementalJs(ob: Can[KeyObfuscator]): List[(String, JsExp)] = Nil
  
  def validate : List[FieldError] = {
    runSafe {
      meta.validate(this)
    }
  }
  
  def asJs: JsExp = {
    meta.asJs(this)
  }
  
  /**
   * Delete the instance from backing store
   */
  def delete_! : Boolean = {
    if (!db_can_delete_?) false else
    runSafe {
      was_deleted_? = meta.delete_!(this)
      was_deleted_?
    }
  }
  
  /**
   * Can this model object be deleted?
   */
  def db_can_delete_? : Boolean =  meta.saved_?(this) && !was_deleted_?
  
  /**
   * Present the model as a form and execute the function on submission of the form
   *
   * @param button - If it's Full, put a submit button on the form with the value of the parameter
   * @param f - the function to execute on form submission
   *
   * @return the form
   */
  def toForm(f: MyType => Unit): NodeSeq =
  meta.toForm(this) ++ (SHtml.hidden(() => f(this)))
  
  /**
   * Find the field by name
   * @param fieldName -- the name of the field to find
   *
   * @return Can[MappedField]
   */ 
  def fieldByName[T](fieldName: String): Can[Field[T, MyType]] = meta.fieldByName[T](fieldName, this)  
}

trait ExpandoRecord[MyType <: Record[MyType] with ExpandoRecord[MyType]] {
  self: MyType =>

  /**
   * If there's a field in this record that defines the locale, return it
   */
  def localeField: Can[LocaleField[MyType]] = Empty
  
  def timeZoneField: Can[TimeZoneField[MyType]] = Empty
  
  def countryField: Can[CountryField[MyType]] = Empty
}


trait KeyedRecord[MyType <: KeyedRecord[MyType, KeyType] with Record[MyType], KeyType] {
  self: MyType =>
  
  def primaryKey: KeyField[KeyType, MyType]
  
  def comparePrimaryKeys(other: MyType) = primaryKey === other.primaryKey
}
