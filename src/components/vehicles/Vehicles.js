import React, { useState, useEffect, Component } from 'react'
import { useHistory, useLocation } from 'react-router-dom'
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
    CFormGroup,
    CLabel,
    CModalFooter,
    CBadge,
    CCollapse,
} from '@coreui/react'

import CIcon from '@coreui/icons-react'
import vehicleService from '../../service/VehicleService';
import { convertTime, getVehicleType, secondsToHHMMSS } from '../../utilities/utility'
import AddVehicle from './AddVehicle'
import EditVehicle from './EditVehicle'


class Vehicles extends Component {

    state = {
        currentPage: 1,
        pageSize: 10,
        vehiclesData: null,
        totalPages: 0,
        totalVehicles: 0,
        search: {},
        showAdd: false,
        showEdit: false,
        showDelete: false,
        editVehicle: {},
        showSuccessMsg: false,
        deleteVehicle: {},
    };

    processVehiclesData = (vehiclesData) => {
        vehiclesData.map((vehicle, index) => {
            vehicle.index = this.state.pageSize * (this.state.currentPage - 1) + index + 1;
        });
        return vehiclesData;
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

    handleEdit = (editVehicle) => {
        const editingVehicle = {
            id: editVehicle.id,
            name: editVehicle.name,
            driverName : editVehicle.driverName,
            maxLoadWeight: editVehicle.maxLoadWeight,
            height: editVehicle.height,
            width: editVehicle.width,
            length: editVehicle.length,
            maxCapacity: editVehicle.maxCapacity,
            averageGasConsume: editVehicle.averageGasConsume,
            gasPrice: editVehicle.gasPrice,
            averageFeeTransport: editVehicle.averageFeeTransport,
            minVelocity: editVehicle.minVelocity,
            maxVelocity: editVehicle.maxVelocity,
            averageVelocity: editVehicle.averageVelocity,
            available: editVehicle.available,
            type: editVehicle.type,
            fixedCost: editVehicle.fixedCost,
            excludeCategories: editVehicle.excludedProductCategories,
            excludedGoodsGroups: editVehicle.excludedGoodsGroups,
        }
        this.setState({
            editVehicle: editingVehicle,
            showEdit: true,
        });
    }

    showDeleteModal = (deleteVehicle) => {
        this.setState({
            showDelete: true,
            deleteVehicle: deleteVehicle,
        });
    }

    setShowDelete = (showDelete) => {
        this.setState({
            showDelete: showDelete
        })
    }

    handleDelete = () => {
        const idVehicle = this.state.deleteVehicle.id;
        vehicleService.delete(idVehicle).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.showSuccessMsg("Delete vehicle " + this.state.deleteVehicle.code + " successfully!");
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

    handleDetail = (detailOrder) => {
        const { history } = this.props;
        history.push({
            pathname: '/vehicles/detail',
            search: 'id=' + detailOrder.id,
            state: { id: detailOrder.id }
        });
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
        vehicleService.search(search).then(response => {
            const data = response.data.data;
            this.setState({
                vehiclesData: data.content,
                currentPage: data.pageable.pageNumber + 1,
                pageSize: data.pageable.pageSize,
                totalPages: data.totalPages,
                totalVehicles: data.totalElements,
                editVehicle: {},
            });
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        let {
            currentPage,
            pageSize,
            vehiclesData,
            totalPages,
            totalVehicles,
        } = this.state;
        if ((vehiclesData != null) && vehiclesData.length > 0)
            vehiclesData = this.processVehiclesData(vehiclesData);
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
                                    <CModalTitle>Delete Vehicle</CModalTitle>
                                </CModalHeader>
                                <CModalBody>
                                    <CCard>
                                        <CCardHeader>
                                            Confirm Delete Vehicle
                                        </CCardHeader>
                                        <CCardBody>
                                            {"Delete vehicle " + this.state.deleteVehicle.name + " may affect other data. Do you really want to delete?"}
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
                                <AddVehicle setShowAdd={this.setShowAdd} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                            {this.state.showEdit &&
                                <EditVehicle editVehicle={this.state.editVehicle} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
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
                                Vehicles Info
                                    <small className="text-muted"> {totalPages} pages</small>
                                <small className="text-muted"> {totalVehicles} Vehicles</small>
                            </CCardHeader>
                            <CCardBody>
                                <CDataTable
                                    items={vehiclesData}
                                    fields={[
                                        { key: 'index', label: 'STT' },
                                        { key: 'name', _classes: 'font-weight-bold', label: 'Tên Xe' },
                                        { key: 'capacity', label: 'Sức chứa(kg/m3)' },
                                        { key: 'averageFeeTransport', label: 'Phí di chuyển(VND/km)' },
                                        { key: 'averageVelocity', label: 'Vận tốc trung bình(km/h)' },
                                        { key: 'driverName', label: 'Tên lái xe' },
                                        { key: 'type', label: 'Loại xe' },
                                        { key: 'ready', label: 'Sẵn sàng' },
                                        { key: 'actions', label: 'Thao tác', }

                                    ]}
                                    hover
                                    striped
                                    bordered
                                    size="lg"
                                    // itemsPerPageSelect
                                    itemsPerPage={pageSize}
                                    // activePage={currentPage}
                                    // clickableRows
                                    scopedSlots={
                                        {
                                            'capacity':
                                                (item) => {
                                                    return (
                                                        <td className="py-2">
                                                            <span>{item.maxLoadWeight + "/" + item.maxCapacity}</span>
                                                        </td>
                                                    )
                                                },

                                            'ready':
                                                (item) => {
                                                    return (
                                                        <td>
                                                            <CBadge size="lg" color={item.available === true ? "success" : "danger"}>
                                                                {item.available === true ? "ready" : "not ready"}
                                                            </CBadge>
                                                        </td>
                                                    )
                                                },
                                            'type':
                                                (item) => {
                                                    return (
                                                        <td className="py-2">
                                                            <span>{getVehicleType(item.type)}</span>
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
export default Vehicles
