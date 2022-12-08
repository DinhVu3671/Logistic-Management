import React, { useState, useEffect, Component } from 'react'
import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CDataTable,
    CRow,
    CPagination,
    CInputGroup,
    CInputGroupPrepend,
    CInputGroupText,
    CInput,
    CButton,
    CInputGroupAppend,
    CContainer,
    CButtonGroup,
    CToastBody,
    CToastHeader,
    CToast,
    CToaster,
    CModalHeader,
    CModalTitle,
    CModal,
    CModalBody,
    CModalFooter,
    CCollapse,
} from '@coreui/react'

import CIcon from '@coreui/icons-react'
import productService from '../../service/ProductService';
import { getProducts, } from '../../utilities/utility'
import AddProduct from './AddProduct'
import EditProduct from './EditProduct'


class Products extends Component {

    state = {
        currentPage: 1,
        pageSize: 10,
        productsData: null,
        totalPages: 0,
        totalProducts: 0,
        search: {},
        showAdd: false,
        showEdit: false,
        showDelete: false,
        editProduct: {},
        showSuccessMsg: false,
        deleteProduct: {},
        details: [],
    };

    processProductsData = (productsData) => {
        productsData.map((product, index) => {
            product.index = this.state.pageSize * (this.state.currentPage - 1) + index + 1;
        });
        return productsData;
    }

    toggleDetails = (index) => {
        const details = this.state.details;
        const position = details.indexOf(index)
        let newDetails = details.slice()
        if (position !== -1) {
            newDetails.splice(position, 1)
        } else {
            newDetails = [...details, index]
        }
        this.setState({
            details: newDetails,
        });
    }

    onPageChanged = (page) => {
        let search = this.state.search;
        search.page = page > 0 ? page : 1;
        search.pageSize = this.state.pageSize;
        this.setState({ search: search });
        this.search(this.state.search);
    };

    reloadData = () => {
        this.search(this.state.search);
    }

    handleEdit = (editProduct) => {
        const editingProduct = {
            id: editProduct.id,
            code: editProduct.code,
            name: editProduct.name,
            price: editProduct.price,
            weight: editProduct.weight,
            length: editProduct.length,
            width: editProduct.width,
            height: editProduct.height,
            capacity: editProduct.capacity,
            excludingProducts: editProduct.excludingProducts,
            goodsGroup: editProduct.goodsGroup,
        }
        this.setState({
            editProduct: editingProduct,
            showEdit: true,
        });
    }

    showDeleteModal = (deleteProduct) => {
        this.setState({
            showDelete: true,
            deleteProduct: deleteProduct,
        });
    }

    setShowDelete = (showDelete) => {
        this.setState({
            showDelete: showDelete
        })
    }

    handleDelete = () => {
        const idProduct = this.state.deleteProduct.id;
        productService.delete(idProduct).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.showSuccessMsg("Delete product " + this.state.deleteProduct.code + " successfully!");
                this.setShowDelete(false);
                this.reloadData();
            }

        })
    }

    handleSearch = () => {
        let search = this.state.search;
        search.page = 1;
        search.pageSize = 5;
        this.setState({ search: search });
        this.search(this.state.search);
    }

    handleSearchNameChange = (event) => {
        let value = event.target.value;
        let search = this.state.search;
        search.name = value;
        this.setState({ search: search });
    }

    setShowAdd = (showAdd) => {
        this.setState({
            showAdd: showAdd
        })
    }

    setShowEdit = (showEdit) => {
        this.setState({
            showEdit: showEdit
        })
    }
    showSuccessMsg = (msg) => {
        this.setState({
            showSuccessMsg: true,
            successMsg: msg,
        })
    }

    setShowSuccessToast = (show) => {
        this.setState({
            showSuccessMsg: show === true ? true : false,
        })
    }

    search(search) {
        productService.search(search).then(response => {
            const data = response.data.data;
            this.setState({
                productsData: data.content,
                currentPage: data.pageable.pageNumber + 1,
                pageSize: data.pageable.pageSize,
                totalPages: data.totalPages,
                totalProducts: data.totalElements,
                editProduct: {},
            });
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        let {
            currentPage,
            pageSize,
            productsData,
            totalPages,
            totalProducts,
        } = this.state;
        if ((productsData != null) && productsData.length > 0)
            productsData = this.processProductsData(productsData);
        const details = this.state.details;
        return (
            <CContainer>
                <CRow>
                    <CCol sm='6'>
                        <CCard>
                            <CModal
                                show={this.state.showDelete}
                                onClose={() => this.setShowDelete(false)}
                                color="danger"
                            >
                                <CModalHeader closeButton>
                                    <CModalTitle>Delete Product</CModalTitle>
                                </CModalHeader>
                                <CModalBody>
                                    <CCard>
                                        <CCardHeader>
                                            Confirm Delete Product
                                        </CCardHeader>
                                        <CCardBody>
                                            {"Delete product " + this.state.deleteProduct.code + " may affect other data. Do you really want to delete?"}
                                        </CCardBody>
                                    </CCard>
                                </CModalBody>
                                <CModalFooter>
                                    <CButton color="success" onClick={() => this.handleDelete()}>Confirm</CButton>{' '}
                                    <CButton color="secondary" onClick={() => this.setShowDelete(false)}>Cancel</CButton>
                                </CModalFooter>
                            </CModal>
                            <CCol sm="12" lg="6">
                                <CToaster position='top-right'>
                                    <CToast
                                        key={'toastSuccess'}
                                        show={this.state.showSuccessMsg}
                                        autohide={4000}
                                        fade={true}
                                        onStateChange={(show) => { this.setShowSuccessToast(show) }}
                                    >
                                        <CToastHeader closeButton>Notification</CToastHeader>
                                        <CToastBody>
                                            {this.state.successMsg}
                                        </CToastBody>
                                    </CToast>
                                </CToaster>
                            </CCol>
                            {this.state.showAdd &&
                                <AddProduct setShowAdd={this.setShowAdd} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                            {this.state.showEdit &&
                                <EditProduct editProduct={this.state.editProduct} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                            <CInputGroup className="input-prepend">
                                <CInputGroupPrepend>
                                    <CInputGroupText>
                                        <CIcon name="cil-magnifying-glass" />
                                    </CInputGroupText>
                                </CInputGroupPrepend>
                                <CInput size="2" type="text" placeholder="Search by name" value={this.state.search.name} onChange={this.handleSearchNameChange} />
                                <CInputGroupAppend>
                                    <CButton color="info" onClick={this.handleSearch}>Search</CButton>
                                </CInputGroupAppend>
                            </CInputGroup>
                        </CCard>
                    </CCol>
                    <CCol sm='2'>
                        <CCard>
                            <CButton type="submit" size="md" color="success" onClick={() => this.setShowAdd(true)}><CIcon name="cil-scrubber" /> Add</CButton>
                        </CCard>
                    </CCol>
                </CRow>
                <CRow>
                    <CCol >
                        <CCard>
                            <CCardHeader>
                                Products Info
                                    <small className="text-muted"> {totalPages} pages</small>
                                <small className="text-muted"> {totalProducts} Products</small>
                            </CCardHeader>
                            <CCardBody>
                                <CDataTable
                                    items={productsData}
                                    fields={[
                                        { key: 'index', label: 'STT' },
                                        { key: 'name', _classes: 'font-weight-bold', label: 'Tên sản phẩm' },
                                        { key: 'code', label: 'Mã sản phẩm' },
                                        { key: 'price', label: 'Đơn giá(VND)' },
                                        { key: 'goodsGroup', label: 'Loại sản phẩm' },
                                        { key: 'weight', label: 'Khối lượng(kg)' },
                                        { key: 'capacity', label: 'Thể tích(m3)' },
                                        {
                                            key: 'actions',
                                            label: 'Thao tác',
                                        }

                                    ]}
                                    hover
                                    striped
                                    bordered
                                    size="lg"
                                    itemsPerPage={pageSize}
                                    scopedSlots={
                                        {
                                            'actions':
                                                (item, index) => {
                                                    return (
                                                        <td className="py-2">
                                                            <CButtonGroup>
                                                                <CButton variant="ghost" color="info" shape="pill" size="sm"
                                                                    onClick={() => { this.toggleDetails(index) }}
                                                                >{details.includes(index) ? 'Hide' : 'Detail'}
                                                                </CButton>
                                                                <CButton variant="ghost" color="warning" shape="pill" size="sm"
                                                                    onClick={() => { this.handleEdit(item) }}
                                                                >Edit
                                                            </CButton>
                                                                <CButton variant="ghost" color="danger" shape="pill" size="sm"
                                                                    onClick={() => { this.showDeleteModal(item) }}
                                                                >Delete
                                                            </CButton>

                                                            </CButtonGroup>
                                                        </td>
                                                    )
                                                },
                                            'goodsGroup':
                                                (item, index) => {
                                                    return (
                                                        <td className="py-2">
                                                            {item.goodsGroup.name}
                                                        </td>
                                                    )
                                                },
                                            'details':
                                                (item, index) => {
                                                    return (
                                                        <CCollapse show={details.includes(index)}>
                                                            <CCardBody >
                                                                <CRow>
                                                                    <CCol sm='2'>{"Kích thước: "}</CCol>
                                                                    <CCol sm='2'>{"Chiều dài: " + item.length + " (m)"}</CCol>
                                                                    <CCol sm='2'>{"Chiều rộng: " + item.width + " (m)"}</CCol>
                                                                    <CCol sm='2'>{"Chiều cao: " + item.height + " (m)"}</CCol>
                                                                </CRow>
                                                                <CRow>
                                                                    <CCol sm='12'>{item.excludingProducts.length === 0 ? "" : ("Không chở chung hàng: " + getProducts(item.excludingProducts))}</CCol>
                                                                </CRow>
                                                            </CCardBody>
                                                        </CCollapse>
                                                    )
                                                }
                                        }
                                    }

                                />
                                <CPagination
                                    activePage={currentPage}
                                    onActivePageChange={(page) => this.onPageChanged(page)}
                                    pages={totalPages}
                                    doubleArrows={true}
                                    align="center"
                                />
                            </CCardBody>
                        </CCard>
                    </CCol>
                </CRow>
            </CContainer>
        )

    }
}
export default Products
