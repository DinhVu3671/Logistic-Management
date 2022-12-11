import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CSelect,
    CInputGroup,
    CInputGroupAppend,
} from '@coreui/react';
import React, { Component } from 'react'
import returnOrderService from '../../../service/ReturnOrderService';
import { getListProductOfOrderItems, hhmmToSeconds, listDeliveryMode, orderItemsHasProduct } from '../../../utilities/utility'
import productService from '../../../service/ProductService';
import CIcon from '@coreui/icons-react';

class AddReturnOrder extends Component {
    constructor(props) {
        super(props)
        this.state = {
            order: this.props.order,
            weight: '',
            capacity: '',
            orderItems: [],
            listProduct: [],
        };

    }

    componentDidMount() {
        const productsData = getListProductOfOrderItems(this.state.order.orderItems);
        console.log("productsData: ", productsData);
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
        // this.updateOrderItems(this.state.orderItems);
        const listProduct = productsData;
        let orderItems = [
            {
                product: productsData[0],
                quantity: 1,
                products: productsData
            }
        ];
        let differenceProducts = listProduct.filter(product => !orderItemsHasProduct(orderItems, product));
        orderItems = orderItems.map((orderItem) => {
            orderItem.products = [...differenceProducts, orderItem.product];
            return orderItem;
        })
        this.setState({
            orderItems: orderItems,
            products: differenceProducts,
        });
        console.log("this.state.orderItems: ", this.state.orderItems);
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

    getMaxQuantity = (product) => {
        return this.state.order.orderItems.find(orderItem => orderItem.product.id === product.id).quantity;
    }

    handleSubmit = () => {
        console.log(this.state)
        let returnOrderData = {
            weight: 0,
            capacity: 0,
            orderItems: this.state.orderItems,
            order: this.props.order,
        }

        returnOrderData.orderItems.map((returnOrderItem, index) => {
            this.updateReturnOrderItem(returnOrderItem);
            returnOrderData.weight += returnOrderItem.weight;
            returnOrderData.capacity += returnOrderItem.capacity;
        })

        returnOrderData.weight = Math.round(returnOrderData.weight * 100) / 100;
        returnOrderData.capacity = Math.round(returnOrderData.capacity * 100) / 100;

        returnOrderService.create(this.state.order.id, returnOrderData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.setShowAdd(false);
                this.props.showSuccessMsg("Create returnOrder " + data.data.id + " successfully");
                this.props.reloadData();
            }

        })
    }

    updateOrderItems = (orderItems) => {
        const listProduct = [...this.state.listProduct];
        console.log("this.state.listProduct:", this.state.listProduct);
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
        let returnOrderItem = orderItems[i];
        returnOrderItem.quantity = event.target.value;
        orderItems[i] = returnOrderItem;
        this.setState({ orderItems: orderItems });
        console.log(this.state);
    }

    handleChangeProduct = (i, event) => {
        let orderItems = this.state.orderItems;
        let returnOrderItem = orderItems[i];
        let productId = parseInt(event.target.value);
        returnOrderItem.product = (this.state.products.find(product => product.id === productId));
        orderItems[i] = returnOrderItem;
        this.updateOrderItems(orderItems);
        console.log(this.state);
    }

    updateReturnOrderItem = (returnOrderItem) => {
        returnOrderItem.weight = Math.round(returnOrderItem.product.weight * returnOrderItem.quantity * 100) / 100;
        returnOrderItem.capacity = Math.round(returnOrderItem.product.capacity * returnOrderItem.quantity * 100) / 100;
    }

    createReturnOrderItems() {
        return this.state.orderItems.map((returnOrderItem, i) =>
            <CFormGroup row className="my-0" key={i}>
                <CCol xs="5">
                    <CFormGroup>
                        <CLabel htmlFor="product">Product</CLabel>
                        <CSelect custom size="md" name="product" id="product" value={returnOrderItem.product.id} onChange={(event) => this.handleChangeProduct(i, event)}>
                            {returnOrderItem.products.map((product, index) => <option key={index} value={product.id}>{product.name}</option>)}
                        </CSelect>
                    </CFormGroup>
                </CCol>
                <CCol xs="5">
                    <CFormGroup>
                        <CLabel htmlFor="quantity">Quantity</CLabel>
                        <CInput type="number" id="quantity" max={this.getMaxQuantity(returnOrderItem.product)} name="quantity" value={returnOrderItem.quantity} onChange={(event) => this.handleChangeQuantity(i, event)} />
                    </CFormGroup>
                </CCol>
                <CCol xs="2">
                    <CIcon name="cil-x" onClick={() => this.removeClick(i)} />
                </CCol>
            </CFormGroup>
        )
    }


    render() {
        return (
            <CCol>
                <CModal
                    show={true}
                    onClose={() => this.props.setShowAdd(false)}
                    color="success"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Create ReturnOrder</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                ReturnOrder Form
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
                                    {this.createReturnOrderItems()}
                                </CFormGroup>
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

export default AddReturnOrder;
