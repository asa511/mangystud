/*
 *  Nimble, an extensive application base for Grails
 *  Copyright (C) 2010 Bradley Beddoes
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
 

import grails.plugins.nimble.InstanceGenerator

import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.AdminsService

import org.mangystud.Action;
import org.mangystud.Context 
import org.mangystud.Realm 
import org.mangystud.State;

/*
 * Allows applications using Nimble to undertake process at BootStrap that are related to Nimbe provided objects
 * such as Users, Role, Groups, Permissions etc.
 *
 * Utilizing this BootStrap class ensures that the Nimble environment is populated in the backend data repository correctly
 * before the application attempts to make any extenstions.
 */
class NimbleBootStrap {

  def grailsApplication
  
  def nimbleService
  def userService
  def adminsService
  
  def createRealm = {name, user, contextNames ->
	  def contexts = contextNames.collect {new Context(name: it)}
	  new Realm(name: name, user: user, contexts: contexts).save(failOnError: true)
  }
  
  def addAction = {realmName, user, title, state, contextNames ->
	  def realm = Realm.findByName(realmName)
	  def contexts = contextNames.collect {
		  Context.findByNameAndRealm(it, realm)
	  }
	  println "adding new action: " + title
	  println "contexts " + contexts.toString()
	  new Action(realm: realm, owner: user, title: title, state:state, contexts: contexts).save(failOnError: true)  
  }

  def init = {servletContext ->

    // The following must be executed
    internalBootStap(servletContext)

    // Execute any custom Nimble related BootStrap for your application below

    // Create example User account
    def user = InstanceGenerator.user()
    user.username = "user"
    user.pass = 'useR123!'
    user.passConfirm = 'useR123!'
    user.enabled = true

    def userProfile = InstanceGenerator.profile()
    userProfile.fullName = "Test User"
    userProfile.owner = user
    user.profile = userProfile

    def savedUser = userService.createUser(user)
    if (savedUser.hasErrors()) {
      savedUser.errors.each {
        log.error(it)
      }
      throw new RuntimeException("Error creating example user")
    }

    // Create example Administrative account
    def admins = Role.findByName(AdminsService.ADMIN_ROLE)
    def admin = InstanceGenerator.user()
    admin.username = "admin"
    admin.pass = "admiN123!"
    admin.passConfirm = "admiN123!"
    admin.enabled = true

    def adminProfile = InstanceGenerator.profile()
    adminProfile.fullName = "Administrator"
    adminProfile.owner = admin
    admin.profile = adminProfile

    def savedAdmin = userService.createUser(admin)
    if (savedAdmin.hasErrors()) {
      savedAdmin.errors.each {
        log.error(it)
      }
      throw new RuntimeException("Error creating administrator")
    }

    adminsService.add(admin)
	
	if (!Realm.count()) {
		createRealm "Work", admin, ["Phone", "Email", "Meeting", "Offline"]
		createRealm "Office", admin, ["Call", "Play", "Chore"]
		
		addAction "Work", admin, 'my first action', State.Next, ["Phone"]  
		addAction "Work", admin, 'my second action', State.WaitingFor, ["Email"]  
		addAction "Work", admin, 'my third action', State.Future, ["Meeting"]  
	}

  }

  def destroy = {

  }

  private internalBootStap(def servletContext) {
    nimbleService.init()
  }
} 
