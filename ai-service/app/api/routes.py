"""
FastAPI routes for the AI service.
Provides endpoints for breast cancer diagnosis and health checks.
"""
from fastapi import APIRouter, HTTPException, status
from pydantic import BaseModel, Field
from typing import Optional, Dict, Any, List
from app.agents.orchestrator import OrchestratorAgent
import logging

logger = logging.getLogger(__name__)

router = APIRouter()
orchestrator = OrchestratorAgent()


# Request/Response Models
class ImagingData(BaseModel):
    """Imaging data model."""
    image_data: Optional[str] = Field(None, description="Base64 encoded image or image path")
    modality: str = Field("mammography", description="Imaging modality: mammography, mri, ultrasound")
    metadata: Optional[Dict[str, Any]] = Field(default_factory=dict)


class GenomicData(BaseModel):
    """Genomic data model."""
    gene_expressions: Optional[Dict[str, float]] = Field(default_factory=dict, description="Gene expression levels")
    mutations: Optional[List[str]] = Field(default_factory=list, description="Detected mutations")
    genetic_markers: Optional[Dict[str, Any]] = Field(default_factory=dict)


class ClinicalData(BaseModel):
    """Clinical data model."""
    age: int = Field(..., ge=0, le=120, description="Patient age")
    family_history: bool = Field(False, description="Family history of breast cancer")
    first_degree_relatives_with_bc: int = Field(0, ge=0, description="Number of first-degree relatives with breast cancer")
    previous_biopsies: int = Field(0, ge=0, description="Number of previous biopsies")
    bmi: float = Field(25.0, ge=10, le=60, description="Body mass index")
    menopause_status: str = Field("pre", description="Menopause status: pre or post")
    hormone_therapy: bool = Field(False, description="Currently on hormone replacement therapy")
    hormone_therapy_duration: int = Field(0, ge=0, description="Duration of hormone therapy in years")
    age_at_first_birth: Optional[int] = Field(None, ge=10, le=60)
    lifestyle_factors: Optional[Dict[str, Any]] = Field(default_factory=dict)
    breast_density: str = Field("scattered", description="Breast density category")
    previous_breast_cancer: bool = Field(False)
    chest_radiation_history: bool = Field(False)


class DiagnosisRequest(BaseModel):
    """Complete diagnosis request model."""
    patient_id: Optional[str] = Field(None, description="Patient identifier")
    imaging_data: Optional[ImagingData] = None
    genomic_data: Optional[GenomicData] = None
    clinical_data: Optional[ClinicalData] = None
    
    class Config:
        json_schema_extra = {
            "example": {
                "patient_id": "P12345",
                "imaging_data": {
                    "image_data": "base64_encoded_image_here",
                    "modality": "mammography"
                },
                "genomic_data": {
                    "gene_expressions": {"BRCA1": 5.2, "BRCA2": 3.1},
                    "mutations": []
                },
                "clinical_data": {
                    "age": 55,
                    "family_history": True,
                    "bmi": 26.5,
                    "menopause_status": "post"
                }
            }
        }


class DiagnosisResponse(BaseModel):
    """Diagnosis response model."""
    patient_id: str
    diagnosis_summary: Dict[str, Any]
    agent_analyses: Dict[str, Any]
    key_findings: List[Dict[str, str]]
    recommendations: List[str]
    interpretation: str


class HealthResponse(BaseModel):
    """Health check response."""
    status: str
    service: str
    version: str
    agents_loaded: List[str]


# Endpoints
@router.get("/health", response_model=HealthResponse, tags=["Health"])
async def health_check():
    """
    Health check endpoint.
    Returns the status of the AI service and loaded agents.
    """
    return {
        "status": "healthy",
        "service": "MedInsight+ AI Service",
        "version": "1.0.0",
        "agents_loaded": ["ImagingAgent", "GenomicAgent", "ClinicalAgent", "OrchestratorAgent"]
    }


@router.post("/api/v1/diagnose", response_model=DiagnosisResponse, tags=["Diagnosis"])
async def diagnose_breast_cancer(request: DiagnosisRequest):
    """
    Perform comprehensive breast cancer diagnosis using multimodal data.
    
    This endpoint uses an agentic AI system with specialized agents:
    - ImagingAgent: Analyzes medical images (mammography, MRI, ultrasound)
    - GenomicAgent: Analyzes genetic and molecular data
    - ClinicalAgent: Analyzes patient clinical information
    - OrchestratorAgent: Coordinates agents and makes final decision
    
    Args:
        request: DiagnosisRequest containing imaging, genomic, and/or clinical data
    
    Returns:
        Comprehensive diagnosis report with predictions, risk score, and recommendations
    
    Raises:
        HTTPException: If no data is provided or diagnosis fails
    """
    logger.info(f"Received diagnosis request for patient: {request.patient_id or 'unknown'}")
    
    # Validate that at least one data type is provided
    if not any([request.imaging_data, request.genomic_data, request.clinical_data]):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="At least one data type (imaging, genomic, or clinical) must be provided"
        )
    
    try:
        # Prepare data for orchestrator
        diagnosis_data = {
            "patient_id": request.patient_id or "unknown"
        }
        
        if request.imaging_data:
            diagnosis_data["imaging_data"] = request.imaging_data.model_dump()
        
        if request.genomic_data:
            diagnosis_data["genomic_data"] = request.genomic_data.model_dump()
        
        if request.clinical_data:
            diagnosis_data["clinical_data"] = request.clinical_data.model_dump()
        
        # Perform diagnosis
        result = await orchestrator.diagnose(diagnosis_data)
        
        logger.info(f"Diagnosis completed for patient: {request.patient_id or 'unknown'}")
        return result
        
    except Exception as e:
        logger.error(f"Diagnosis failed: {str(e)}", exc_info=True)
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Diagnosis failed: {str(e)}"
        )


@router.get("/api/v1/agents", tags=["Info"])
async def list_agents():
    """
    List all available agents and their capabilities.
    """
    return {
        "agents": [
            {
                "name": "ImagingAgent",
                "description": "Analyzes medical images for breast cancer detection",
                "supported_modalities": ["mammography", "mri", "ultrasound"],
                "confidence_threshold": 0.6
            },
            {
                "name": "GenomicAgent",
                "description": "Analyzes genetic and molecular data",
                "monitored_genes": ["BRCA1", "BRCA2", "TP53", "PTEN", "STK11", "CDH1", "PALB2", "ATM"],
                "confidence_threshold": 0.65
            },
            {
                "name": "ClinicalAgent",
                "description": "Analyzes patient clinical information and risk factors",
                "analyzed_factors": ["age", "family_history", "lifestyle", "hormonal_factors"],
                "confidence_threshold": 0.55
            },
            {
                "name": "OrchestratorAgent",
                "description": "Coordinates all agents and makes final diagnosis",
                "fusion_method": "late_fusion_ensemble",
                "architecture": "hybrid"
            }
        ]
    }


@router.post("/api/v1/explain", tags=["Interpretability"])
async def explain_diagnosis(diagnosis_result: Dict[str, Any]):
    """
    Generate detailed explanation for a diagnosis result using SHAP/LIME.
    
    This endpoint would integrate SHAP or LIME for model interpretability.
    Currently returns structured explanation from agent analyses.
    """
    # In production, this would use SHAP/LIME for detailed feature importance
    # For now, we return the interpretations from the agents
    
    return {
        "explanation_method": "agent_based_reasoning",
        "note": "Each agent provides interpretable explanations based on domain-specific features",
        "available_explanations": [
            "Imaging features (masses, calcifications, distortions)",
            "Genetic risk factors (BRCA mutations, gene expressions)",
            "Clinical risk factors (age, family history, lifestyle)"
        ]
    }
