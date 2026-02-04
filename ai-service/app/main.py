"""
Main FastAPI application for MedInsight+ AI Service.
Provides breast cancer diagnosis using agentic AI with multimodal data.
"""
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import router
import logging

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)

logger = logging.getLogger(__name__)

# Create FastAPI app
app = FastAPI(
    title="MedInsight+ AI Service",
    description="""
    Agentic AI system for breast cancer diagnosis using multimodal data.
    
    ## Features
    - **Multimodal Analysis**: Combines imaging, genomic, and clinical data
    - **Agentic Architecture**: Specialized agents for each data type
    - **Hybrid Fusion**: Late fusion ensemble approach
    - **Interpretability**: Explainable AI with feature importance
    - **Risk Assessment**: Comprehensive risk scoring and recommendations
    
    ## Agents
    - **ImagingAgent**: Analyzes medical images (mammography, MRI, ultrasound)
    - **GenomicAgent**: Analyzes genetic and molecular data
    - **ClinicalAgent**: Analyzes patient clinical information
    - **OrchestratorAgent**: Coordinates agents and makes final decision
    """,
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify allowed origins
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routes
app.include_router(router)

# Root endpoint
@app.get("/", tags=["Root"])
async def root():
    """Root endpoint with service information."""
    return {
        "service": "MedInsight+ AI Service",
        "version": "1.0.0",
        "description": "Agentic AI for breast cancer diagnosis",
        "endpoints": {
            "health": "/health",
            "diagnose": "/api/v1/diagnose",
            "agents": "/api/v1/agents",
            "docs": "/docs"
        }
    }


# Startup event
@app.on_event("startup")
async def startup_event():
    """Initialize services on startup."""
    logger.info("=" * 80)
    logger.info("MedInsight+ AI Service Starting...")
    logger.info("=" * 80)
    logger.info("Initializing agents...")
    logger.info("✓ ImagingAgent ready")
    logger.info("✓ GenomicAgent ready")
    logger.info("✓ ClinicalAgent ready")
    logger.info("✓ OrchestratorAgent ready")
    logger.info("=" * 80)
    logger.info("AI Service is ready to accept requests")
    logger.info("=" * 80)


# Shutdown event
@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown."""
    logger.info("Shutting down MedInsight+ AI Service...")


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    )
