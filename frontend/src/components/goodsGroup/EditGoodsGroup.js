import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CSelect,
    CTextarea,
} from '@coreui/react';
import React, { Component } from 'react'
import goodsGroupService from '../../service/GoodsGroupService';
import { listGoodsGroupCategory } from '../../utilities/utility'
import MultiSelect from "@khanacademy/react-multi-select";

class EditGoodsGroup extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.editGoodsGroup.id,
            name: this.props.editGoodsGroup.name,
            detail: this.props.editGoodsGroup.detail,
        };

    }

    componentDidMount() {
        this.setState({
            id: this.props.editGoodsGroup.id,
            name: this.props.editGoodsGroup.name,
            detail: this.props.editGoodsGroup.detail,
        })
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
        let goodsGroupData = {
            id: this.state.id,
            name: this.state.name,
            detail: this.state.detail,
        }
        goodsGroupService.update(goodsGroupData.id, goodsGroupData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.setShowEdit(false);
                this.props.showSuccessMsg("Edit goodsGroup " + data.data.name + " successfully");
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
                        <CModalTitle>Edit GoodsGroup</CModalTitle>
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
                        <CButton color="secondary" onClick={() => this.props.setShowEdit(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default EditGoodsGroup;
