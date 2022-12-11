import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CSelect,
    CInputGroup,
    CInputGroupAppend,
} from '@coreui/react';
import React, { Component } from 'react'
import depotService from '../../service/DepotService';
import customerService from '../../service/CustomerService';
import orderService from '../../service/OrderService';
import { getProduct, hhmmToSeconds, listDeliveryMode, listOrderCategory, orderItemsHasProduct, removeProduct } from '../../utilities/utility'
import productService from '../../service/ProductService';
import CIcon from '@coreui/icons-react';

class AddOrder extends Component {
    constructor(props) {
        super(props)
        this.state = {
            depotId: '',
            depots: [],
            customerId: '',
            customers: [],
            weight: '',
            capacity: '',
            deliveryMode: listDeliveryMode[0].code,
            deliveryModes: listDeliveryMode,
            orderItems: [],
            deliveryBeforeTime: '',
            deliveryAfterTime: '',
            productTypeNumber: '',
        };

    }

    componentDidMount() {
        let search = { paged: false };
        depotService.search(search).then(response => {
            const depotsData = response.data.data;
            this.setState({
                depots: depotsData,
                depotId: depotsData[0].id,
            });
        }).catch(e => {
            console.log(e);
        });
        customerService.search(search).then(response => {
            const customersData = response.data.data;
            this.setState({
                customers: customersData,
                customerId: customersData[0].id,
            });
        }).catch(e => {
            console.log(e);
        });
        productService.search(search).then(response => {
            const productsData = response.data.data;
            this.setState({
                listProduct: productsData,
                products: productsData,
                orderItems: [
                    {
                        product: productsData[0],
                        quantity: 1,
                        products: productsData
                    }
                ],
            });
            this.updateOrderItems(this.state.orderItems);
        }).catch(e => {
            console.log(e);
        });
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    setShowToast = (showToast) => {
        this.setState({
            showToast: showToast
        });
    }

    handleSubmit = () => {
        console.log(this.state)
        let orderData = {
            // depot: { id: this.state.depotId },
            customer: { id: this.state.customerId },
            weight: 0,
            capacity: 0,
            orderValue: 0,
            orderItems: this.state.orderItems,
            deliveryMode: this.state.deliveryMode,
            timeService: this.state.timeService,
            timeLoading: this.state.timeLoading,
            deliveryBeforeTime: hhmmToSeconds(this.state.deliveryBeforeTime),
            deliveryAfterTime: hhmmToSeconds(this.state.deliveryAfterTime),
        }

        orderData.orderItems.map((orderItem, index) => {
            this.updateOrderItem(orderItem);
            orderData.weight += orderItem.weight;
            orderData.capacity += orderItem.capacity;
            orderData.orderValue += orderItem.price;
        })

        orderData.weight = Math.round(orderData.weight * 100) / 100;
        orderData.capacity = Math.round(orderData.capacity * 100) / 100;
        orderData.orderValue = Math.round(orderData.orderValue * 100) / 100;

        orderService.create(orderData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.setShowAdd(false);
                this.props.showSuccessMsg("Add order " + data.data.code + " successfully");
                this.props.reloadData();
            }

        })
    }

    updateOrderItems = (orderItems) => {
        const listProduct = [...this.state.listProduct];
        let differenceProducts = listProduct.filter(product => !orderItemsHasProduct(orderItems, product));
        orderItems = orderItems.map((orderItem, index) => {
            orderItem.products = [...differenceProducts, orderItem.product];
            return orderItem;
        })
        this.setState({
            orderItems: orderItems,
            products: differenceProducts,
        });
    }

    addClick = () => {
        let orderItems = this.state.orderItems;
        orderItems.push({
            product: this.state.products[0],
            quantity: 1
        });
        this.updateOrderItems(orderItems);
        console.log(this.state);
    }

    removeClick(i) {
        let orderItems = this.state.orderItems;
        orderItems.splice(i, 1);
        this.updateOrderItems(orderItems);
        console.log(this.state);
    }

    handleChangeQuantity = (i, event) => {
        let orderItems = this.state.orderItems;
        let orderItem = orderItems[i];
        orderItem.quantity = event.target.value;
        orderItems[i] = orderItem;
        this.setState({ orderItems: orderItems });
        console.log(this.state);
    }

    handleChangeProduct = (i, event) => {
        let orderItems = this.state.orderItems;
        let orderItem = orderItems[i];
        let productId = parseInt(event.target.value);
        orderItem.product = (this.state.products.find(product => product.id === productId));
        orderItems[i] = orderItem;
        this.updateOrderItems(orderItems);
        console.log(this.state);
    }

    updateOrderItem = (orderItem) => {
        orderItem.price = Math.round(orderItem.product.price * orderItem.quantity * 100) / 100;
        orderItem.weight = Math.round(orderItem.product.weight * orderItem.quantity * 100) / 100;
        orderItem.capacity = Math.round(orderItem.product.capacity * orderItem.quantity * 100) / 100;
    }

    createOrderItems() {
        return this.state.orderItems.map((orderItem, i) =>
            <CFormGroup row className="my-0" key={i}>
                <CCol xs="5">
                    <CFormGroup>
                        <CLabel htmlFor="product">Product</CLabel>
                        <CSelect custom size="md" name="product" id="product" value={orderItem.product.id} onChange={(event) => this.handleChangeProduct(i, event)}>
                            {orderItem.products.map((product, index) => <option key={index} value={product.id}>{product.name}</option>)}
                        </CSelect>
                    </CFormGroup>
                </CCol>
                <CCol xs="5">
                    <CFormGroup>
                        <CLabel htmlFor="quantity">Quantity</CLabel>
                        <CInput type="number" id="quantity" name="quantity" value={orderItem.quantity} onChange={(event) => this.handleChangeQuantity(i, event)} />
                    </CFormGroup>
                </CCol>
                <CCol xs="2">
                    <CIcon name="cil-x" onClick={() => this.removeClick(i)} />
                </CCol>
            </CFormGroup>
        )
    }


    render() {
        const depots = this.state.depots;
        const customers = this.state.customers;
        const deliveryModes = this.state.deliveryModes;
        return (
            <CCol>
                <CModal
                    show={true}
                    onClose={() => this.props.setShowAdd(false)}
                    color="success"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Create Order</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Order Form
                                        </CCardHeader>
                            <CCardBody>
                                {/* <CFormGroup>
                                    <CLabel htmlFor="depot">Depot</CLabel>
                                    <CSelect custom size="md" name="depotId" id="depot" value={this.state.depotId} onChange={this.handleInputChange}>
                                        {depots.map((depot, index) => <option key={index} value={depot.id}>{depot.name}</option>)}
                                    </CSelect>
                                </CFormGroup> */}
                                <CFormGroup>
                                    <CLabel htmlFor="customer">Customer</CLabel>
                                    <CSelect custom size="md" name="customerId" id="customer" value={this.state.customerId} onChange={this.handleInputChange}>
                                        {customers.map((customer, index) => <option key={index} value={customer.id}>{customer.name}</option>)}
                                    </CSelect>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CLabel htmlFor="deliveryMode">Delivery Mode</CLabel>
                                        <CSelect custom size="md" name="deliveryMode" id="deliveryMode" value={this.state.deliveryMode} onChange={this.handleInputChange}>
                                            {deliveryModes.map((deliveryMode, index) => <option key={index} value={deliveryMode.code}>{deliveryMode.description}</option>)}
                                        </CSelect>
                                    </CCol>
                                    <CCol xs="4">
                                        <CLabel htmlFor="timeService">Time Service(s)</CLabel>
                                        <CInput type="number" custom size="md" name="timeService" id="timeService" value={this.state.timeService} placeholder="timeService" onChange={this.handleInputChange}>
                                        </CInput>
                                    </CCol>
                                    <CCol xs="4">
                                        <CLabel htmlFor="timeLoading">Time Loading(s)</CLabel>
                                        <CInput type="number" custom size="md" name="timeLoading" id="timeLoading" value={this.state.timeLoading} placeholder="timeLoading" onChange={this.handleInputChange}>
                                        </CInput>
                                    </CCol>
                                </CFormGroup>
                                {(this.state.deliveryMode !== "STANDARD") &&
                                    <CFormGroup row className="my-0">
                                        <CCol xs="6">
                                            <CFormGroup>
                                                <CLabel htmlFor="deliveryAfterTime">Delivery After Time</CLabel>
                                                <CInput type="time" id="deliveryAfterTime" name="deliveryAfterTime" value={this.state.deliveryAfterTime} onChange={this.handleInputChange} />
                                            </CFormGroup>
                                        </CCol>
                                        <CCol xs="6">
                                            <CFormGroup>
                                                <CLabel htmlFor="deliveryBeforeTime">Delivery Before Time</CLabel>
                                                <CInput type="time" id="deliveryBeforeTime" name="deliveryBeforeTime" value={this.state.deliveryBeforeTime} onChange={this.handleInputChange} />
                                            </CFormGroup>
                                        </CCol>
                                    </CFormGroup>
                                }
                                &nbsp;
                                <CCard>
                                    <CCardHeader>
                                        Order Items
                                    </CCardHeader>
                                    <CCardBody>
                                        {(this.state.listProduct) &&
                                            <CFormGroup row className="my-0">
                                                <CCol xs="6">
                                                    <CInputGroup>
                                                        <CButton disabled={!(this.state.orderItems.length < this.state.listProduct.length)} type="button" color="primary" onClick={() => this.addClick()}>Add</CButton>
                                                    </CInputGroup>
                                                </CCol>
                                            </CFormGroup>
                                        }
                                        <CFormGroup className="my-0">
                                            {this.createOrderItems()}
                                        </CFormGroup>
                                    </CCardBody>
                                </CCard>
                            </CCardBody>
                        </CCard>
                    </CModalBody>
                    <CModalFooter>
                        <CButton color="success" onClick={() => this.handleSubmit()}>Submit</CButton>{' '}
                        <CButton color="secondary" onClick={() => this.props.setShowAdd(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default AddOrder;
