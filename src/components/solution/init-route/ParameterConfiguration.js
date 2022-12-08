import CIcon from "@coreui/icons-react";
import { CButton, CCard, CCardBody, CCardHeader, CCol, CCollapse, CFormGroup, CInput, CLabel, CRow, CSpinner, CSwitch } from "@coreui/react";
import React, { Component } from "react";
import { withNamespaces } from 'react-i18next';

class ParameterConfiguration extends Component {

    constructor(props) {
        super(props)
        this.state = {
            problemAssumption: {},
            accordions: [0],
        };

    }

    setAccordions = (accordions) => {
        this.setState({ accordions: accordions })
    }

    addAccordion = (accordion) => {
        let accordions = this.state.accordions;
        accordions.push(accordion);
        this.setAccordions(accordions);
    }

    removeAccordion = (accordion) => {
        let accordions = this.state.accordions;
        accordions = accordions.filter(item => item !== accordion)
        this.setAccordions(accordions);
    }

    checkAccordion = (accordion) => {
        return this.state.accordions.includes(accordion);
    }

    updateAccordions = (accordion) => {
        if (this.checkAccordion(accordion) === true)
            this.removeAccordion(accordion);
        else
            this.addAccordion(accordion);
    }

    render() {
        const { t,values } = this.props;
        return (
            <CCard>
                <CCardHeader>
                {t('parameterConfiguration.title')}
                    <CRow className="card-header-actions">
                        {this.props.isWaiting === true &&
                            <div>
                                <CSpinner size='sm' color='green'></CSpinner>
                                &nbsp;
                                {t('parameterConfiguration.preparingData')}
                                &nbsp;
                            </div>
                        }
                        <CButton disabled={this.props.isWaiting} onClick={() => this.props.nextStep()} size="sm" color="success"> {t('parameterConfiguration.next')} <CIcon name="cil-arrow-right" /></CButton>
                    </CRow>
                </CCardHeader>
                <CCardBody>
                    <CCard className="mb-0">
                        <CCardHeader id="headingOne">
                            <CButton
                                block
                                color="link"
                                className="text-left m-0 p-0"
                                onClick={() => this.updateAccordions(0)}
                            >
                                <h5 className="m-0 p-0"> {t('parameterConfiguration.basic.title')}</h5>
                            </CButton>
                        </CCardHeader>
                        <CCollapse show={this.checkAccordion(0) === true}>
                            <CCardBody>
                                <CFormGroup row xs="10" className="my-0">
                                    <CCol xs="2">
                                        <CFormGroup>
                                            <CRow>
                                                <CLabel htmlFor="isLimitedTime"> {t('parameterConfiguration.basic.travelingTimeLimit')}</CLabel>
                                            </CRow>
                                            <CSwitch id='isLimitedTime' name="isLimitedTime" size="lg" checked={values.isLimitedTime} color={'success'} onChange={this.props.handleInputChange} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                        </CFormGroup>
                                    </CCol>
                                    {values.isLimitedTime === true &&
                                        <CCol xs="3">
                                            <CFormGroup>
                                                <CLabel htmlFor="maxTravelTime"> {t('parameterConfiguration.basic.maxDrivingTime')}</CLabel>
                                                <CInput id="maxTravelTime" name="maxTravelTime" value={values.maxTravelTime} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.basic.maxDrivingTimePlace')} min="0" max="10" type='number' step="0.01" />
                                            </CFormGroup>
                                        </CCol>
                                    }
                                    <CCol xs="2">
                                        <CFormGroup>
                                            <CRow>
                                                <CLabel htmlFor="isLimitedDistance"> {t('parameterConfiguration.basic.travelingDistanceLimit')}</CLabel>
                                            </CRow>
                                            <CSwitch id='isLimitedDistance' name="isLimitedDistance" size="lg" checked={values.isLimitedDistance} color={'success'} onChange={this.props.handleInputChange} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                        </CFormGroup>
                                    </CCol>
                                    {values.isLimitedDistance === true &&
                                        <CCol xs="3">
                                            <CFormGroup>
                                                <CLabel htmlFor="maxDistance"> {t('parameterConfiguration.basic.maxDrivingDistance')}</CLabel>
                                                <CInput id="maxDistance" name="maxDistance" value={values.maxDistance} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.basic.maxDrivingDistancePlace')} type='number' min="0" max="1000" step="0.1" />
                                            </CFormGroup>
                                        </CCol>
                                    }
                                </CFormGroup>

                                {/* <CFormGroup row xs="10" className="my-0">
                                    <CCol xs="2">
                                        <CFormGroup>
                                            <CRow>
                                                <CLabel htmlFor="isLimitedNumNode">Giới hạn số đơn hàng</CLabel>
                                            </CRow>
                                            <CSwitch id='isLimitedNumNode' name="isLimitedNumNode" size="lg" checked={values.isLimitedNumNode} color={'success'} onChange={this.props.handleInputChange} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="3">
                                        <CFormGroup>
                                            <CLabel htmlFor="limitedNumNode">Số đơn hàng tối đa trên một xe</CLabel>
                                            <CInput id="limitedNumNode" name="limitedNumNode" value={values.limitedNumNode} onChange={this.props.handleInputChange} placeholder="limitedNumNode" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
 */}
                                <CFormGroup row xs="10" className="my-0">
                                    <CCol xs="3">
                                        <CFormGroup>
                                            <CLabel htmlFor="maxTime"> {t('parameterConfiguration.basic.maximumAlgorithmRunningTime')}</CLabel>
                                            <CInput id="maxTime" name="maxTime" value={values.maxTime} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.basic.maximumAlgorithmRunningTimePlace')} min="0" max ="40" type='number' step="0.1" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="3">
                                        <CFormGroup>
                                            <CRow> 
                                                <CLabel htmlFor="isAllowedViolateTW."> {t('parameterConfiguration.basic.isAllowedViolateTW')}</CLabel>
                                            </CRow>
                                            <CSwitch id='isAllowedViolateTW' name="isAllowedViolateTW" size="lg" checked={values.isAllowedViolateTW} color={'success'} onChange={this.props.handleInputChange} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="2">
                                        <CFormGroup>
                                            <CRow>
                                                <CLabel htmlFor="isExcludeProduct"> {t('parameterConfiguration.basic.isExcludeProduct')}</CLabel>
                                            </CRow>
                                            <CSwitch id='isExcludeProduct' name="isExcludeProduct" size="lg" checked={values.isExcludeProduct} color={'success'} onChange={this.props.handleInputChange} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                            </CCardBody>
                        </CCollapse>
                    </CCard>
                    <CCard className="mb-0">
                        <CCardHeader id="headingTwo">
                            <CButton
                                block
                                color="link"
                                className="text-left m-0 p-0"
                                onClick={() => this.updateAccordions(1)}
                            >
                                <h5 className="m-0 p-0"> {t('parameterConfiguration.advance.title')}</h5>
                            </CButton>
                        </CCardHeader>
                        <CCollapse show={this.checkAccordion(1) === true}>
                            <CCardBody>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="popSize"> {t('parameterConfiguration.advance.popSize')}</CLabel>
                                            <CInput id="popSize" name="popSize" value={values.popSize} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.popSizePlace')} min="10" max ="500" type='number' step="1" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="4">
                                        <CFormGroup>
                                            {/* <CLabel htmlFor="eliteSize">Kích thước tập tinh hoa (cá thể)</CLabel>
                                            <CInput id="eliteSize" name="eliteSize" value={values.eliteSize} onChange={this.props.handleInputChange} placeholder="eliteSize" type='number' step="1" /> */}
                                            <CLabel htmlFor="eliteRate"> {t('parameterConfiguration.advance.eliteRate')}</CLabel>
                                            <CInput id="eliteRate" name="eliteRate" value={values.eliteRate} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.eliteRatePlace')} min="1" max ="25" type='number' step="0.1" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="maxGen"> {t('parameterConfiguration.advance.maxGen')}</CLabel>
                                            <CInput id="maxGen" name="maxGen" value={values.maxGen} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.maxGenPlace')} min="10" max ="500" type='number' step="1" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="maxGenImprove"> {t('parameterConfiguration.advance.maxGenNotImprove')}</CLabel>
                                            <CInput id="maxGenImprove" name="maxGenImprove" value={values.maxGenImprove} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.maxGenNotImprovePlace')} min="20" max ="150" type='number' step="1" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="probCrossover"> {t('parameterConfiguration.advance.probCrossover')}</CLabel>
                                            <CInput id="probCrossover" name="probCrossover" value={values.probCrossover} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.probCrossoverPlace')} min="0.1" max ="0.99" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="probMutation"> {t('parameterConfiguration.advance.probMutation')}</CLabel>
                                            <CInput id="probMutation" name="probMutation" value={values.probMutation} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.probMutationPlace')} min="0.1" max ="0.99" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="tournamentSize"> {t('parameterConfiguration.advance.tournamentSize')}</CLabel>
                                            <CInput id="tournamentSize" name="tournamentSize" value={values.tournamentSize} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.tournamentSizePlace')}min="5" max ="20" type='number' step="1" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="selectionRate"> {t('parameterConfiguration.advance.selectionRate')}</CLabel>
                                            <CInput id="selectionRate" name="selectionRate" value={values.selectionRate} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.selectionRatePlace')} min="1" max ="40" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="4">
                                        <CFormGroup>
                                            <CLabel htmlFor="exponentialFactor"> {t('parameterConfiguration.advance.exponentialFactor')}</CLabel>
                                            <CInput id="exponentialFactor" name="exponentialFactor" value={values.exponentialFactor} onChange={this.props.handleInputChange} placeholder= {t('parameterConfiguration.advance.exponentialFactorPlace')} min="1" max ="5" type='number' step="1" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                            </CCardBody>
                        </CCollapse>
                    </CCard>
                </CCardBody>
            </CCard>
        );
    }

}

export default withNamespaces('solution')(ParameterConfiguration);