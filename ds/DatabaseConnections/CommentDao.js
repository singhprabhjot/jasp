var mongoose = require('mongoose');
var dbConnection = require('./DBConnection');

var db = dbConnection.createConnection;

var offer = require('../DatabaseConnections/OfferDao');
var uuid = require('node-uuid');
//var cmt = require("../DatabaseConnections/CommentDao");
//var db = mongoose.connect("mongodb://localhost:27017/nodetest2");

var CommentSchema = dbConnection.CommentSchema;

var CommentModel = db.model('Comment', offer.CommentSchema);
var OfferModel = db.model('Offer', offer.OfferSchema);

function CommentDao() {
}


CommentDao.prototype.postComment = function(callback, offerId, comment, userId) {

			var commentId = uuid.v4();
			commentId = commentId.substr(commentId.length - 5);
			var comment = new CommentModel({
				CommentId: commentId,
				Comment : comment,
				userId : userId,
			});
			
			comment.save(function(err, comments) {
				if (!err) {
					console.log('comment created');
					callback(null, comment);
				} else {
					console.log(err);
					callback('comment not posted', null);
				}

			});
			

};






CommentDao.prototype.getCommentHistory = function(callback, OfferId) {

	OfferModel.find({
		OfferId : OfferId
	}, function(err, offers) {
		if (!err) {
			console.log('offer id exists');
			CommentModel.find({
				OfferId : OfferId
			},function(err, comments) {
				callback(err, comments);
			});
		} else {
			console.log(err);
			callback('offer id does not exist ', null);
		}
	});

};




module.exports = CommentDao