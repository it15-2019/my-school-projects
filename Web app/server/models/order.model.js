const mongoose = require('mongoose');
const { stringify } = require('querystring');
const OrderShema = new mongoose.Schema({
    userId: {
        type: String,
        required: true,
    },
    orderTotal: {
        type: Number,
        required: true,
    },

    orderSubTotal: {
        type: Number,
        required: true,
    },

    itemsCount: {
        type: Number,
        required: true,
    },

    shippingCost: {
        type: Number,
        required: true,
    },
    
    estimatedTax: {
        type: Number,
        required: true,
    },
    itemList : [
        {
            id: 'string',
            price: 'number',
            name: 'string',
            imgUrl: 'string',
            quantity: 'number',
            itemTotal: 'number',
        }
    ],
    cartId: {
        type: String,
        required: true,
    },
    createdAt: {
        type: Date,
        default: Date.now,
    },
    deliveryDate: {
        type: Date,
        default: Date.now,
    },
    versionKey: false,
});

module.exports = mongoose.model('Order', OrderShema);