import { CButton, CCard, CCardBody, CCardFooter, CCardHeader, CContainer, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CProgress, CToast, CToastBody, CToaster, CToastHeader } from "@coreui/react";
import React, { Component } from "react";
import { withNamespaces } from 'react-i18next';

class Progress extends Component {

    constructor(props) {
        super(props)
        this.state = {
            isProgress: this.props.values.isProgress,
            progressPercent: this.props.values.progressPercent,
            showToast: this.props.values.isInitFail,
            showConfirm: true,
        };

    }

    handleConfirm = () => {
        this.props.handleGetRoutes();
    }

    handleCancel = () => {
        this.props.goToStep(0);
    }


    setShowToast = (showToast) => {
        this.setState({
            showToast: showToast
        });
    }

    handleCancelReInit = () => {
        this.setShowConfirm(false);
    }

    handleConfirmReInit = () => {
        this.props.handleReInitWithoutConstraint();
    }

    setShowConfirm = (showConfirm) => {
        this.setState({
            showConfirm: showConfirm
        });
    }

    render() {
        const { t } = this.props;
        return (
            <CContainer>
                {(this.props.values.isInitFail === true) &&
                    <CModal
                        show={this.state.showConfirm}
                        onClose={() => this.setShowConfirm(false)}
                        color="warning"
                    >
                        <CModalHeader closeButton>
                            <CModalTitle>{t("progress.routeReInitialization.title")}</CModalTitle>
                        </CModalHeader>
                        <CModalBody>
                            <CCard>
                                <CCardBody>
                                    <p>
                                        {t("progress.routeReInitialization.body")}
                                    </p>
                                    <i>{t("progress.routeReInitialization.note")}</i>
                                </CCardBody>
                            </CCard>
                        </CModalBody>
                        <CModalFooter>
                            <CButton color="success" onClick={() => this.handleConfirmReInit()}>{t("progress.routeReInitialization.yes")}</CButton>{' '}
                            <CButton color="secondary" onClick={() => this.handleCancelReInit()}>{t("progress.routeReInitialization.cancel")}</CButton>
                        </CModalFooter>
                    </CModal>
                }
                {(this.props.values.isProgress === false) &&
                    <CCard>
                        <CCardHeader>
                            {t("progress.routeInitialization.title")}
                        </CCardHeader>
                        <CCardBody>
                            <p><b>{t("progress.routeInitialization.body")}</b></p>
                            <small><i>{t("progress.routeInitialization.note")}</i></small>
                        </CCardBody>
                        <CCardFooter>
                            <CButton color="success" disabled={this.props.isWaiting} onClick={() => this.handleConfirm()}>{t("progress.routeInitialization.yes")}</CButton>{' '}
                            <CButton color="secondary" onClick={() => this.handleCancel(false)}>{t("progress.routeInitialization.cancel")}</CButton>
                        </CCardFooter>
                    </CCard>
                }
                {(this.props.values.isProgress === true) &&
                    <CCard>
                        <CCardHeader>
                            {t("progress.inProgress.title")}
                        </CCardHeader>
                        <CCardBody>
                            <CProgress animated precision={this.props.values.progressPercent === 100 ? 0 : 1} showPercentage step="0.01" value={this.props.values.progressPercent} className="mb-3" />
                        </CCardBody>
                    </CCard>
                }
                {(this.props.values.isInitFail === true) &&
                    <CToaster position='top-right'>
                        <CToast
                            key={'toastSuccess'}
                            show={true}
                            autohide={4000}
                            fade={true}
                            onStateChange={(showToast) => { this.setShowToast(showToast) }}
                        >
                            <CToastHeader closeButton>{t("progress.initFail.title")}</CToastHeader>
                            <CToastBody>
                                <p>
                                    {t("progress.initFail.body")}
                                </p>
                                <i>  {t("progress.initFail.note")}</i>
                            </CToastBody>
                        </CToast>
                    </CToaster>
                }
            </CContainer>
        );
    }

}

export default withNamespaces('solution')(Progress);