package org.mangystud


class Tiddler {

    static constraints = {
		realm(nullable:false)
		title(size:3..100, blank: false, unique:true)
		notes(nullable:true)
		owner(nullable:false)
    }
	
	static mappings = {
		realm lazy: false
	 }
	
	static hasMany = [ contexts : Context ]
	
	Realm realm
	String title
	String notes
	User owner

	Date lastUpdated
	Date dateCreated
}
