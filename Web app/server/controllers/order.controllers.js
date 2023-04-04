const Order = require("../models/order.model");

async function submitOrder(order) 
{   
    console.log('saving order to database', order);

    await new Order(order).save();
}

module.exports = {
    submitOrder
};