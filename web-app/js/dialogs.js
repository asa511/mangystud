Dialog = function(){
	this.name = "_dialog";
};

$.extend(Dialog.prototype, {
	init : function() {
		this.el = $('#' + this.name);
		$.data(this.el[0], 'parent', this);
		this.el.dialog({
		    autoOpen: false,
			buttons: {
				"OK" : this.onSubmit,
				"Cancel" : this.onCancel
			},
			resizable: false,
			modal: true
		});
		return this;
	},

	onSubmit: function() {
		var me = $.data(this, 'parent');
		if (me && me.isValid()) {
			$('form', this).submit();
		}
	},
	
	isValid : function() { return true },
	
	onCancel : function() {
		$(this).dialog("close");		
	},
	
	close : function() {
		$(this.el).dialog("close");		
	},
	
	show : function(event) {
		this.beforeShow(event);
		this.el.dialog("open");
	}, 
	
	beforeShow : function() {}
});

RealmDialog = function() {
	this.name = 'realm_dialog';
	return this;
};

RealmDialog.prototype = $.extend({}, Dialog.prototype, {
	isValid : function() {
		var name = $('[name="name"]', this.el).val();
		if (name.trim() === "") {
			alert('Please enter a name');
			return false;
	  }
	  return true;
	},

	beforeShow : function(event) {
		$('[name="name"]', this.el).val('');
		this.el.dialog("option", "title", 'New Realm');
	}, 
	
	onSuccess : function(data, textStatus) {
		this.close();

		var span = $('<span/>', {
			class: "realm-tab realm-active",
			text: data.realm.name
		});
		$('.realm-add').before(span);
	}
});

ContextDialog = function() {
	this.name = 'context_dialog';
	return this;
};

ContextDialog.prototype = $.extend({}, Dialog.prototype, {
	isValid : function() {
		var name = $('[name=name]', this.el).val();
		if (name.trim() === "") {
			alert('Please enter a name');
			return false;
	  }
	  return true;
	},

	beforeShow : function(event) {
		var actionId = manager.getCurrentActionId();
		if (actionId) {
			var tiddler = manager.currentTiddler;
			var realm = $('.realm_select select', tiddler).val();
	
			$('[name=realm]', this.el).val(realm);
			$('[name=actionId]', this.el).val(actionId);
			$('[name=name]', this.el).val('');
			this.el.dialog("option", "title", 'New Context');
		} else {
			alert('Unable to get action id!');
		}
	}, 
	
	onSuccess : function(data, textStatus) {
		this.close();
		updateContextDivs(data);
	}
});

ActionDialog = function() {
	this.name = 'action_dialog';
	return this;
};

ActionDialog.prototype = $.extend({}, Dialog.prototype, {
	isValid : function() {
		var name = $('[name=title]', this.el).val();
		if (name.trim() === "") {
			alert('Please enter a title');
			return false;
	  }
	  return true;
	},

	beforeShow : function(event) {
		var obj = event.currentTarget;
		$('[name=type]').val(obj.id);
		$('[name=title]').val('');
		this.el.dialog("option", "title", 'New ' + obj.id);
	}, 
	
	onSuccess : function(data, textStatus) {
		this.close();
		tl_viewAction(data.action.id);
		manager.raiseEvent('actionUpdate', {event: 'new', id: data.action.id});
	}
});

