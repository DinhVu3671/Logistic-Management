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
    CCollapse,
} from '@coreui/react'

import CIcon from '@coreui/icons-react'
import goodsGroupService from '../../service/GoodsGroupService';
import { convertTime, getCategory, getGoodsGroups, secondsToHHMMSS } from '../../utilities/utility'
import AddGoodsGroup from './AddGoodsGroup'
import EditGoodsGroup from './EditGoodsGroup'


class GoodsGroups extends Component {

    state = {
        currentPage: 1,
        pageSize: 10,
        goodsGroupsData: null,
        totalPages: 0,
        totalGoodsGroups: 0,
        search: {},
        showAdd: false,
        showEdit: false,
        showDelete: false,
        editGoodsGroup: {},
        showSuccessMsg: false,
        deleteGoodsGroup: {},
        details: [],
    };

    processGoodsGroupsData = (goodsGroupsData) => {
        goodsGroupsData.map((goodsGroup, index) => {
            goodsGroup.index = this.state.pageSize * (this.state.currentPage - 1) + index + 1;
        });
        return goodsGroupsData;
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

    handleEdit = (editGoodsGroup) => {
        const editingGoodsGroup = {
            id: editGoodsGroup.id,
            name: editGoodsGroup.name,
            detail: editGoodsGroup.detail,
        }
        this.setState({
            editGoodsGroup: editingGoodsGroup,
            showEdit: true,
        });
    }

    showDeleteModal = (deleteGoodsGroup) => {
        this.setState({
            showDelete: true,
            deleteGoodsGroup: deleteGoodsGroup,
        });
    }

    setShowDelete = (showDelete) => {
        this.setState({
            showDelete: showDelete
        })
    }

    handleDelete = () => {
        const idGoodsGroup = this.state.deleteGoodsGroup.id;
        goodsGroupService.delete(idGoodsGroup).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.showSuccessMsg("Delete goodsGroup " + this.state.deleteGoodsGroup.code + " successfully!");
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
        goodsGroupService.search(search).then(response => {
            const data = response.data.data;
            this.setState({
                goodsGroupsData: data.content,
                currentPage: data.pageable.pageNumber + 1,
                pageSize: data.pageable.pageSize,
                totalPages: data.totalPages,
                totalGoodsGroups: data.totalElements,
                editGoodsGroup: {},
            });
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        let {
            currentPage,
            pageSize,
            goodsGroupsData,
            totalPages,
            totalGoodsGroups,
        } = this.state;
        if ((goodsGroupsData != null) && goodsGroupsData.length > 0)
            goodsGroupsData = this.processGoodsGroupsData(goodsGroupsData);
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
                                    <CModalTitle>Delete GoodsGroup</CModalTitle>
                                </CModalHeader>
                                <CModalBody>
                                    <CCard>
                                        <CCardHeader>
                                            Confirm Delete GoodsGroup
                                        </CCardHeader>
                                        <CCardBody>
                                            {"Delete goodsGroup " + this.state.deleteGoodsGroup.code + " may affect other data. Do you really want to delete?"}
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
                                <AddGoodsGroup setShowAdd={this.setShowAdd} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                            {this.state.showEdit &&
                                <EditGoodsGroup editGoodsGroup={this.state.editGoodsGroup} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
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
                                GoodsGroups Info
                                    <small className="text-muted"> {totalPages} pages</small>
                                <small className="text-muted"> {totalGoodsGroups} GoodsGroups</small>
                            </CCardHeader>
                            <CCardBody>
                                <CDataTable
                                    items={goodsGroupsData}
                                    fields={[
                                        { key: 'index', label: 'STT' },
                                        { key: 'name', _classes: 'font-weight-bold', label: 'Tên nhóm' },
                                        { key: 'detail', label: 'Chi tiết' },
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
export default GoodsGroups
