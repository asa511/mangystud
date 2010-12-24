package org.mangystud

class Contact {

    static constraints = {
		name(size:1..100, blank:false, unique:true)
		email(email:true, nullable:true)
    }
	
	static belongsTo = [realm:Realm]

	String name;
	String email;
}