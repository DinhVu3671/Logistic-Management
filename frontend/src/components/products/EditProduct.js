import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CSelect,
} from '@coreui/react';
import React, { Component } from 'react'
import productService from '../../service/ProductService';
import MultiSelect from "@khanacademy/react-multi-select";
import goodsGroupsService from '../../service/GoodsGroupService';

class EditProduct extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.editProduct.id,
            name: this.props.editProduct.name,
            price: this.props.editProduct.price,
            weight: this.props.editProduct.weight,
            capacity: this.props.editProduct.capacity,
            excludingProducts: this.props.editProduct.excludingProducts,
            goodsGroup: this.props.editProduct.goodsGroup,
            goodsGroups: [],
        };

    }

    componentDidMount() {
        this.setState({
            id: this.props.editProduct.id,
            name: this.props.editProduct.name,
            price: this.props.editProduct.price,
            weight: this.props.editProduct.weight,
            length: this.props.editProduct.length,
            width: this.props.editProduct.width,
            height: this.props.editProduct.height,
            capacity: this.props.editProduct.capacity,
            goodsGroupId: this.props.editProduct.goodsGroup.id,
        })
        let search = { paged: false };
        productService.search(search).then(response => {
            let productsData = response.data.data;
            let excludingProducts = [];
            let products = [];
            productsData.map((product, index) => {
                this.state.excludingProducts.map((excludingProduct) => {
                    if (excludingProduct.id === product.id)
                        excludingProducts.push(product);
                })
                if (this.state.id !== product.id) {
                    products.push({
                        label: product.name + " - " + product.code,
                        value: product,
                    });
                }
            })
            this.setState({
                products: products,
                excludingProducts: excludingProducts,
            })
        }).catch(e => {
            console.log(e);
        });

        goodsGroupsService.search(search).then(response => {
            let goodsGroups = response.data.data;
            this.setState({
                goodsGroups: goodsGroups,
            });
        });
    }

    handleChangeExcludeProduct = (excludeProducts) => {
        console.log(excludeProducts)
        this.setState({ excludingProducts: excludeProducts });
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    handleSubmit = () => {
        console.log(this.state)
        let productData = {
            id: this.state.id,
            name: this.state.name,
            price: parseFloat(this.state.price),
            weight: parseFloat(this.state.weight),
            length: parseFloat(this.state.length),
            width: parseFloat(this.state.width),
            height: parseFloat(this.state.height),
            capacity: Math.round(parseFloat(this.state.length * this.state.width * this.state.height) * 100) / 100,
            excludingProducts: this.state.excludingProducts,
            goodsGroup: this.state.goodsGroups.find(goodsGroup => goodsGroup.id === parseInt(this.state.goodsGroupId)),
        }
        console.log("productData:", productData)
        productService.update(productData.id, productData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.setShowEdit(false);
                this.props.showSuccessMsg("Edit product " + data.data.code + " successfully");
                this.props.reloadData();
            }

        })
    }


    render() {
        return (
            <CCol>
                <CModal
                    show={true}
                    onClose={() => this.props.setShowEdit(false)}
                    color="warning"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Edit Product</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Product Form
                                        </CCardHeader>
                            <CCardBody>
                                <CFormGroup>
                                    <CLabel htmlFor="productName">Name</CLabel>
                                    <CInput id="productName" name="name" value={this.state.name} onChange={this.handleInputChange} placeholder="Enter your product name" />
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="goodsGroupId">GoodsGroup</CLabel>
                                    <CSelect custom size="md" name="goodsGroupId" id="goodsGroupId" value={this.state.goodsGroupId} onChange={this.handleInputChange}>
                                        {this.state.goodsGroups.map((goodsGroup, index) => <option key={index} value={goodsGroup.id}>{goodsGroup.name}</option>)}
                                    </CSelect>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="price">Price(VND)</CLabel>
                                            <CInput id="price" name="price" value={this.state.price} onChange={this.handleInputChange} placeholder="price" type='number' />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="weight">Weight (kg)</CLabel>
                                            <CInput id="weight" name="weight" value={this.state.weight} onChange={this.handleInputChange} placeholder="weight" type='number' step="0.1" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="length">Length (m)</CLabel>
                                            <CInput id="length" name="length" value={this.state.length} onChange={this.handleInputChange} placeholder="length" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="width">Width (m)</CLabel>
                                            <CInput id="width" name="width" value={this.state.width} onChange={this.handleInputChange} placeholder="width" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="height">Height (m)</CLabel>
                                            <CInput id="height" name="height" value={this.state.height} onChange={this.handleInputChange} placeholder="height" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="excludingProducts">Excluding Products</CLabel>
                                    {this.state.products &&
                                        <MultiSelect
                                            options={this.state.products}
                                            selected={this.state.excludingProducts}
                                            onSelectedChanged={selected => this.handleChangeExcludeProduct(selected)}
                                        />
                                    }
                                </CFormGroup>
                            </CCardBody>
                        </CCard>
                    </CModalBody>
                    <CModalFooter>
                        <CButton color="success" onClick={() => this.handleSubmit()}>Submit</CButton>{' '}
                        <CButton color="secondary" onClick={() => this.props.setShowEdit(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default EditProduct;
