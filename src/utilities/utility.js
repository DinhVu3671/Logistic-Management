import { element } from "prop-types";

const convertIntToString = (number) => {
    return number < 10 ? ("0" + number) : number;
}

const getFormatDate = (dateTime) => {
    var d = new Date(dateTime);
    return d.getDate() + "-" + (d.getMonth() + 1) + "-" + d.getFullYear();
}

const secondsToHHMMSS = (ms) => {
    var seconds = ms;
    var hours = parseInt(seconds / 3600);
    seconds = seconds % 3600;
    var minutes = parseInt(seconds / 60);
    seconds = seconds % 60;
    let humanized = convertIntToString(hours) + ":" + convertIntToString(minutes) + ":" + convertIntToString(seconds);
    return humanized;
}

const hhmmToSeconds = (hhmm) => {
    var a = hhmm.split(':');
    var seconds = (+a[0]) * 60 * 60 + (+a[1]) * 60;
    return seconds;
}

const convertTime = (items) => {
    items.map((item, index) => {
        item.startTime = secondsToHHMMSS(item.startTime);
        item.endTime = secondsToHHMMSS(item.endTime);
    });
}

const secondsToHHMM = (seconds) => {
    var hours = parseInt(seconds / 3600);
    seconds = seconds % 3600;
    var minutes = parseInt(seconds / 60);
    let humanized = convertIntToString(hours) + ":" + convertIntToString(minutes);
    return humanized;
}

const listProductCategory = [
    {
        code: "AVOID_MOISTURE",
        description: "tránh ẩm"
    },
    {
        code: "PERISHABLE",
        description: "hàng dễ hỏng"
    },
    {
        code: "AVOID_HEAT",
        description: "tránh nhiệt độ cao"
    },
    {
        code: "AVOID_STACKING",
        description: "tránh xếp chồng"
    },
];

const listVehicleType = [
    {
        code: "TRUCK",
        description: "xe tải"
    },
    {
        code: "BIKE",
        description: "xe máy"
    },
]

const listDeliveryMode = [
    {
        code: "STANDARD",
        description: "tiêu chuẩn"
    },
    {
        code: "MEDIUM",
        description: "trung bình"
    },
    {
        code: "FAST",
        description: "nhanh"
    },
];

const getVehicleType = (code) => {
    return listVehicleType.map((type, index) => {
        if (type.code === code)
            return type.description;
    })
}

const getCategories = (categories) => {
    let listString = "";
    categories.map((category, index) => {
        for (let i = 0; i < listProductCategory.length; i++) {
            if (category === listProductCategory[i].code)
                listString += ", " + listProductCategory[i].description;
        }
    })
    return listString.slice(1, listString.length);
}

const getProducts = (products) => {
    let listString = "";
    products.map((product, index) => {
        listString += ", " + product.name;
    })
    return listString.slice(1, listString.length);
}

const getOrdinalLoadingOrders = (ordinalLoadingOrders) => {
    let listString = "";
    ordinalLoadingOrders.map((loadingOrder, index) => {
        listString += "->" + loadingOrder;
    })
    return listString.slice(2, listString.length);
}

const getCategory = (code) => {
    return listProductCategory.map((category, index) => {
        if (category.code === code)
            return category.description;
    })
}

const getDeliveryMode = (code) => {
    return listDeliveryMode.map((deliveryMode, index) => {
        if (deliveryMode.code === code)
            return deliveryMode.description;
    })
}

const getDeliveryModeColor = status => {
    switch (status) {
        case 'STANDARD': return 'secondary'
        case 'MEDIUM': return 'warning'
        case 'FAST': return 'danger'
        default: return 'primary'
    }
}

const getProduct = (products, productId) => {
    return products.map((product, index) => {
        if (product.id === productId)
            return product;
    })
}

const addProduct = (products, product) => {
    products.push(product);
}

const removeProduct = (products, product) => {
    let index = products.findIndex(element => element.id === product.id);
    products.splice(index, 1);
    return product;
}

const orderItemsHasProduct = (orderItems, product) => {
    for (let index = 0; index < orderItems.length; index++) {
        const element = orderItems[index];
        if (element.product.id === product.id)
            return true;
    }
    return false;
}

const getIconURL = (type) => {
    switch (type) {
        case "default":
            return "";
        case "TRUCK":
            return "http://maps.google.com/mapfiles/kml/shapes/truck.png";
        case "BIKE":
            return "http://maps.google.com/mapfiles/kml/shapes/motorcycling.png";
        case "DEPOT":
            return "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png";
        default:
            return "";
    }
}

const getCustomersOfJourney = (journey) => {
    let customers = [];
    journey.routes.map((route, ind) => {
        route.orders.map((order, index) => {
            customers.push(order.customer)
        });
    });
    return customers;
}

const getDepotsOfJourney = (journey) => {
    let depots = [];
    journey.routes.map((route, ind) => {
        depots.push(route.startDepot);
        depots.push(route.endDepot);
    });
    return depots;
}

const checkExistNode = (checkedNode, nodes) => {
    for (let index = 0; index < nodes.length; index++) {
        const node = nodes[index];
        if (node.code === checkedNode.code)
            return index;
    }
    return null;
}

const checkExistProduct = (checkedProduct, products) => {
    for (let index = 0; index < products.length; index++) {
        const product = products[index];
        if (product.id === checkedProduct.id)
            return index;
    }
    return null;
}

const getListProductOfOrderItems = (orderItems) => {
    const productList = [];
    orderItems.map((orderItem) => {
        productList.push(orderItem.product);
    })
    const products = [];
    productList.map((product) => {
        if (checkExistProduct(product, products) === null) {
            products.push(product);
        }
    });
    return products;
}

export {
    removeProduct, orderItemsHasProduct, secondsToHHMMSS, convertTime, hhmmToSeconds, secondsToHHMM,
    listProductCategory, getCategory, listDeliveryMode, getDeliveryMode, getDeliveryModeColor, getProduct,
    listVehicleType, getVehicleType, getCategories, getProducts, getOrdinalLoadingOrders, getFormatDate,
    getIconURL, getCustomersOfJourney, getDepotsOfJourney, checkExistNode, getListProductOfOrderItems
};
