const express = require('express');
const asyncHandler = require('express-async-handler');
const userController = require('../controllers/user.controller');
const authController = require('../controllers/auth.controller');
const passport = require('../middleware/passport');
const router = express.Router();


// localhost:4050/api/auth/register 
router.post('/register', asyncHandler(insert), login);
// localhost:4050/api/auth/login
router.post('/login', passport.authenticate('local', {session: false}), login);
//router.post('/login', asyncHandler(getUserByEmailIdAndPassword), login);
// localhost:4050/api/auth/findme
router.get('/findme', passport.authenticate('jwt', {session: false}), login);

async function insert(req, res, next) 
{   
    const user = req.body;
    console.log('registering user', user);
    req.user = await userController.insert(user);
    next();
}

function login(req, res)
{
    const user = req.user;
    const token = authController.generateToken(user);
    res.json({ user, token });
}

/* async function getUserByEmailIdAndPassword(req, res, next)
{
    const user = req.body;
    console.log('searching user for', user);
    const savedUser = await userController.getUserByEmailIdAndPassword(user.email, user.password);
    req.user = savedUser;
    next();
} */

module.exports = router;