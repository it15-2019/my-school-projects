const mongoose = require('mongoose');

const UserSchema = new mongoose.Schema({
    fullname: 
    {
        type: String,
        required: true
    }, 
    email: 
    {
        type: String,
        required: true,
        unique: true, 
        match: [/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/, 'Please fill a valid email address']
    },
    hashedPassword: 
    {
        type: String,
        required: true,
    },
    createdAt: 
    {
        type: Date,
        defualt: Date.now
    },
    roles: 
    [{
        type: String
    }],
    versionKey: false
});

module.exports = mongoose.model('User', UserSchema);