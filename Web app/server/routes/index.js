const express = require('express');
const authRoutes = require('./auth.route');
const orderRoutes = require('./order.route');
const router = express.Router();

// localhost:4050/api/auth 
router.use('/auth', authRoutes);

// localhost:4050/api/auth /orders
router.use('/orders', orderRoutes);

module.exports = router;