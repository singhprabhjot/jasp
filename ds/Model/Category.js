var CategoryDao = require("../DatabaseConnections/CategoryDao");
var categoryobj = new CategoryDao();

var ejs = require("ejs");

function Category() {

}

Category.prototype.createCategory = function(callback,request)
{
	
	console.log(request);
	categoryobj.createCategory(function(err,res) {
		callback(err,res);
		
	},request.categoryName);

};

Category.prototype.viewCategories = function(callback,request)
{
	
	console.log("view categories function ");
	categoryobj.viewCategories(function(err,res) {
		callback(err,res);
		
	});

};

Category.prototype.getCategoryById = function(callback,categoryId){
	
	
	categoryobj.getCategoryById(function(err,res){
		callback(err,res);
	},categoryId);
};


module.exports = Category;
