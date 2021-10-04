function Category (id, name) {
    this.setId(id);
    this.setName(name);
    this.setParentCategory();
}

Category.create = function(e){
	var retCat = new Category(e.id, e.name);
	retCat.setParentCategory(e.parentCategory);
	return retCat;
}

Category.prototype.setId = function(id) {
	if(typeof id === 'undefined')
		id = "";
	
	this.id = id;
};

Category.prototype.getId = function() {
	 return this.id;
};

Category.prototype.setName = function(name) {
	if(typeof name === 'undefined')
		name = "";
	
	this.name = name;
};

Category.prototype.setParentCategory = function(parentCategory) {
	if(typeof parentCategory === 'undefined')
		parentCategory = "";
	
	this.parentCategory = parentCategory;
};

Category.prototype.getId = function() {
	 return this.id;
};

Category.prototype.getName = function() {
	 return this.name;
};

Category.prototype.getParentCategory = function() {
	 return this.parentCategory;
};
