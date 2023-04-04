const express = require('express');
const orderController = require('../controllers/order.controllers');
const asyncHandler = require('express-async-handler');

const router = express.Router();

// localhost:4050/api/auth/orders/submit
router.post('/submit', asyncHandler(orderController.submitOrder));

async function submitOrder(req, res, next)
{
    const orderToSave = req.body;
    console.log('Received order to save is', orderToSave);

    const order = await orderController.submitOrder(orderToSave);
    
    res.json(order);
}

module.exports = router;