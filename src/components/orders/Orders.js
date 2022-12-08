import React, { useState, useEffect, Component } from 'react'
import { Link, useHistory, useLocation } from 'react-router-dom'
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
    CBadge,
} from '@coreui/react'

import CIcon from '@coreui/icons-react'
import orderService from '../../service/OrderService';
import { getDeliveryMode, getDeliveryModeColor } from '../../utilities/utility'
import AddOrder from './AddOrder'
import EditOrder from './EditOrder'


class Orders extends Component {

    state = {
        currentPage: 1,
        pageSize: 10,
        ordersData: null,
        totalPages: 0,
        totalOrders: 0,
        search: {},
        showAdd: false,
        showEdit: false,
        showDelete: false,
        editOrder: {},
        showSuccessMsg: false,
        deleteOrder: {},
    };

    processOrdersData = (ordersData) => {
        ordersData.map((order, index) => {
            order.index = this.state.pageSize * (this.state.currentPage - 1) + index + 1;
        });
        return ordersData;
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

    handleDetail = (detailOrder) => {
        const { history } = this.props;
        history.push({
            pathname: '/orders/detail',
            search: 'id=' + detailOrder.id,
            state: { id: detailOrder.id }
        });
    }

    handleEdit = (editOrder) => {
        const editingOrder = {
            id: editOrder.id,
            code: editOrder.code,
            depot: editOrder.depot,
            customer: editOrder.customer,
            deliveryMode: editOrder.deliveryMode,
            orderItems: editOrder.orderItems,
            orderValue: editOrder.orderValue,
            deliveryBeforeTime: editOrder.deliveryBeforeTime,
            deliveryAfterTime: editOrder.deliveryAfterTime,
            productTypeNumber: editOrder.productTypeNumber,
            timeService: editOrder.timeService,
            timeLoading: editOrder.timeLoading,
            weight: editOrder.weight,
            capacity: editOrder.capacity,
        }
        this.setState({
            editOrder: editingOrder,
            showEdit: true,
        });
    }

    showDeleteModal = (deleteOrder) => {
        this.setState({
            showDelete: true,
            deleteOrder: deleteOrder,
        });
    }

    setShowDelete = (showDelete) => {
        this.setState({
            showDelete: showDelete
        })
    }

    handleDelete = () => {
        const idOrder = this.state.deleteOrder.id;
        orderService.delete(idOrder).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.showSuccessMsg("Delete order " + this.state.deleteOrder.code + " successfully!");
                this.setShowDelete(false);
                this.reloadData();
            }

        })
    }

    handleSearch = () => {
        let search = this.state.search;
        search.page = 1;
        search.pageSize = 10;
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
        orderService.search(search).then(response => {
            const data = response.data.data;
            this.setState({
                ordersData: data.content,
                currentPage: data.pageable.pageNumber + 1,
                pageSize: data.pageable.pageSize,
                totalPages: data.totalPages,
                totalOrders: data.totalElements,
                editOrder: {},
            });
        }).catch(e => {
            console.log(e);
        });
    }

    createOrderData() {
        orderService.createOrderData().then(response => {
            console.log(response);
        })
    }

    render() {
        let {
            currentPage,
            pageSize,
            ordersData,
            totalPages,
            totalOrders,
        } = this.state;
        if ((ordersData != null) && ordersData.length > 0)
            ordersData = this.processOrdersData(ordersData);
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
                                    <CModalTitle>Delete Order</CModalTitle>
                                </CModalHeader>
                                <CModalBody>
                                    <CCard>
                                        <CCardHeader>
                                            Confirm Delete Order
                                        </CCardHeader>
                                        <CCardBody>
                                            {"Delete order " + this.state.deleteOrder.code + " may affect other data. Do you really want to delete?"}
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
                                <AddOrder setShowAdd={this.setShowAdd} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                            {this.state.showEdit &&
                                <EditOrder editOrder={this.state.editOrder} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
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
                        {/* <CCard>
                            <CButton type="submit" size="md" color="success" onClick={() => this.createOrderData()}><CIcon name="cil-scrubber" /> Create Order Data</CButton>
                        </CCard> */}
                    </CCol>
                </CRow>
                <CRow>
                    <CCol >
                        <CCard>
                            <CCardHeader>
                                Orders Info
                                    <small className="text-muted"> {totalPages} pages</small>
                                <small className="text-muted"> {totalOrders} Orders</small>
                            </CCardHeader>
                            <CCardBody>
                                <CDataTable
                                    items={ordersData}
                                    fields={[
                                        { key: 'index', label: 'STT' },
                                        { key: 'code', label: 'Mã đơn hàng' },
                                        // { key: 'depotCode', label: 'Mã kho hàng' },
                                        { key: 'customerCode', label: 'Mã khách hàng' },
                                        { key: 'orderValue', label: 'Giá trị đơn hàng(VND)' },
                                        { key: 'weight', label: 'Khối lượng(kg)' },
                                        { key: 'capacity', label: 'Thể tích(m3)' },
                                        { key: 'deliveryMode', label: 'Chế độ giao hàng' },
                                        { key: 'productTypeNumber', label: 'Số loại sản phẩm' },
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
                                            'depotCode':
                                                (item) => {
                                                    return (
                                                        <td className="py-2">
                                                            <span>{item.depot.code}</span>
                                                        </td>
                                                    )
                                                },
                                            'customerCode':
                                                (item) => {
                                                    return (
                                                        <td className="py-2">
                                                            <span>{item.customer.code}</span>
                                                        </td>
                                                    )
                                                },

                                            'deliveryMode':
                                                (item) => {
                                                    return (
                                                        <td>
                                                            <CBadge size="lg" color={getDeliveryModeColor(item.deliveryMode)}>
                                                                {getDeliveryMode(item.deliveryMode)}
                                                            </CBadge>
                                                        </td>
                                                    )
                                                },

                                            'actions':
                                                (item, index) => {
                                                    return (
                                                        <td className="py-2">
                                                            <CButtonGroup>
                                                                <CButton variant="ghost" color="info" shape="pill" size="sm"
                                                                    onClick={() => { this.handleDetail(item) }}
                                                                >Detail
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
export default Orders
