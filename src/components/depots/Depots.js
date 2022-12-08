import React, { Component } from 'react'
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
} from '@coreui/react'

import CIcon from '@coreui/icons-react'
import depotService from '../../service/DepotService';
import { secondsToHHMMSS } from '../../utilities/utility'
import AddDepot from './AddDepot'
import EditDepot from './EditDepot'


class Depots extends Component {

    state = {
        currentPage: 1,
        pageSize: 5,
        depotsData: null,
        totalPages: 0,
        totalDepots: 0,
        search: {},
        showAdd: false,
        showEdit: false,
        showDelete: false,
        editDepot: {},
        showSuccessMsg: false,
        deleteDepot: {},
    };

    processDepotsData = (depotsData) => {
        depotsData.map((depot, index) => {
            depot.startTimeStr = secondsToHHMMSS(depot.startTime);
            depot.endTimeStr = secondsToHHMMSS(depot.endTime);
            depot.index = this.state.pageSize * (this.state.currentPage - 1) + index + 1;
        });
        return depotsData;
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

    handleDetail = (detailDepot) => {
        const { history } = this.props;
        history.push({
            pathname: '/depots/detail',
            search: 'id=' + detailDepot.id,
            state: { id: detailDepot.id }
        });
    }

    handleEdit = (editDepot) => {
        const editingDepot = {
            id: editDepot.id,
            code: editDepot.code,
            name: editDepot.name,
            address: editDepot.address,
            latitude: editDepot.latitude,
            longitude: editDepot.longitude,
            startTime: editDepot.startTime,
            endTime: editDepot.endTime,
            unloadingCost: editDepot.unloadingCost,
            products: editDepot.products,
        }
        this.setState({
            editDepot: editingDepot,
            showEdit: true,
        });
    }

    showDeleteModal = (deleteDepot) => {
        this.setState({
            showDelete: true,
            deleteDepot: deleteDepot,
        });
    }

    setShowDelete = (showDelete) => {
        this.setState({
            showDelete: showDelete
        })
    }

    handleDelete = () => {
        const idDepot = this.state.deleteDepot.id;
        depotService.delete(idDepot).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.showSuccessMsg("Delete depot " + this.state.deleteDepot.code + " successfully!");
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
        depotService.search(search).then(response => {
            const data = response.data.data;
            this.setState({
                depotsData: data.content,
                currentPage: data.pageable.pageNumber + 1,
                pageSize: data.pageable.pageSize,
                totalPages: data.totalPages,
                totalDepots: data.totalElements,
                editDepot: {},
            });
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        let {
            currentPage,
            pageSize,
            depotsData,
            totalPages,
            totalDepots,
        } = this.state;
        if ((depotsData != null) && depotsData.length > 0)
            depotsData = this.processDepotsData(depotsData);
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
                                    <CModalTitle>Delete Depot</CModalTitle>
                                </CModalHeader>
                                <CModalBody>
                                    <CCard>
                                        <CCardHeader>
                                            Confirm Delete Depot
                                        </CCardHeader>
                                        <CCardBody>
                                            {"Delete depot " + this.state.deleteDepot.code + " may affect other data. Do you really want to delete?"}
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
                            <AddDepot setShowAdd={this.setShowAdd} show={this.state.showAdd} reloadData={this.reloadData} />
                            {this.state.showEdit &&
                                <EditDepot editDepot={this.state.editDepot} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
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
                                Depots Info
                                    <small className="text-muted"> {totalPages} pages</small>
                                <small className="text-muted"> {totalDepots} Depots</small>
                            </CCardHeader>
                            <CCardBody>
                                <CDataTable
                                    items={depotsData}
                                    fields={[
                                        { key: 'index', label: 'STT' },
                                        { key: 'name', _classes: 'font-weight-bold', label: 'Tên kho' },
                                        { key: 'code', label: 'Mã kho' },
                                        { key: 'longitude', label: 'Kinh độ' },
                                        { key: 'latitude', label: 'Vĩ độ' },
                                        { key: 'address', label: 'Địa chỉ' },
                                        { key: 'startTimeStr', label: 'Thời điểm mở cửa' },
                                        { key: 'endTimeStr', label: 'Thời điểm đóng cửa' },
                                        // { key: 'unloadingCost', label: 'Phí dỡ hàng(VND)' },
                                        {
                                            key: 'actions',
                                            label: 'Thao tác',
                                        }

                                    ]}
                                    hover
                                    striped
                                    bordered
                                    size="lg"
                                    // itemsPerPageSelect
                                    itemsPerPage={pageSize}
                                    // activePage={currentPage}
                                    // clickableRows
                                    scopedSlots={{
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
                                    }}

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
export default Depots
