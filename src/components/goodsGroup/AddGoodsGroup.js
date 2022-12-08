import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CSelect,
    CTextarea,
} from '@coreui/react';
import React, { Component } from 'react'
import goodsGroupService from '../../service/GoodsGroupService';
import { listGoodsGroupCategory } from '../../utilities/utility'
import MultiSelect from "@khanacademy/react-multi-select";


class AddGoodsGroup extends Component {
    constructor(props) {
        super(props)
        this.state = {
            name: '',
            detail: '',
        };

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
        let goodsGroupData = {
            name: this.state.name,
            detail: this.state.detail,
        }
        goodsGroupService.create(goodsGroupData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.setShowAdd(false);
                this.props.showSuccessMsg("Add goodsGroup " + data.data.name + " successfully");
                this.props.reloadData();
            }

        })
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
                        <CModalTitle>Create GoodsGroup</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                GoodsGroup Form
                                        </CCardHeader>
                            <CCardBody>
                                <CFormGroup>
                                    <CLabel htmlFor="goodsGroupName">Name</CLabel>
                                    <CInput id="goodsGroupName" name="name" value={this.state.name} onChange={this.handleInputChange} placeholder="Enter your goodsGroup name" />
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="goodsGroupDetail">Detail</CLabel>
                                    <CTextarea id="goodsGroupDetail" name="detail" value={this.state.detail} onChange={this.handleInputChange} placeholder="Enter your goodsGroup detail" />
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

export default AddGoodsGroup;
